package com.pzhu.article.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pzhu.article.domain.*;
import com.pzhu.article.feign.UserInfoFeignService;
import com.pzhu.article.mapper.StrategyContentMapper;
import com.pzhu.article.mapper.StrategyMapper;
import com.pzhu.article.qo.StrategyQuery;
import com.pzhu.article.redis.key.StrategyRedisKeyPrefix;
import com.pzhu.article.service.DestinationService;
import com.pzhu.article.service.StrategyCatalogService;
import com.pzhu.article.service.StrategyService;
import com.pzhu.article.service.StrategyThemeService;
import com.pzhu.article.utils.OssUtil;
import com.pzhu.article.vo.StrategyCondition;
import com.pzhu.auth.util.AuthenticationUtils;
import com.pzhu.core.exception.BusinessException;
import com.pzhu.core.utils.DateUtils;
import com.pzhu.core.utils.R;
import com.pzhu.redis.utils.RedisCache;
import com.pzhu.user.vo.LoginUser;
import org.apache.ibatis.transaction.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


@Service
public class StrategyServiceImpl extends ServiceImpl<StrategyMapper, Strategy> implements StrategyService {
    private final StrategyThemeService strategyThemeService;
    private final StrategyCatalogService strategyCatalogService;
    private final DestinationService destinationService;
    private final StrategyContentMapper strategyContentMapper;
    private final RedisCache redisCache;
    private final UserInfoFeignService userInfoFeignService;

    public StrategyServiceImpl(DestinationService destinationService, StrategyCatalogService strategyCatalogService, StrategyThemeService strategyThemeService, StrategyContentMapper strategyContentMapper, RedisCache redisCache,
                               @Lazy UserInfoFeignService userInfoFeignService) {
        this.destinationService = destinationService;
        this.strategyCatalogService = strategyCatalogService;
        this.strategyThemeService = strategyThemeService;
        this.strategyContentMapper = strategyContentMapper;
        this.redisCache = redisCache;
        this.userInfoFeignService = userInfoFeignService;
    }

    /**
     * 查询攻略对象，设置攻略内容
     * @param id
     * @return
     */
    @Override
    public Strategy getById(Serializable id) {
        Strategy strategy = super.getById(id);
        StrategyContent content = strategyContentMapper.selectById(id);
        strategy.setContent(content);
        // 查询当前用户是否已收藏
        LoginUser user = AuthenticationUtils.getUser();
        if (user != null){
            // 通过远程调用得到用户收藏文章 id 集合
            R<List<Long>> favoriteStrategyIdList = userInfoFeignService.getFavorStrategyIdList(user.getId());
            List<Long> list = favoriteStrategyIdList.getAndCheck();
            strategy.setFavorite(list.contains(id));
        }
        // 从 redis 中查询最新的统计数据
        Map<String, Object> statData = redisCache.getCacheMap(StrategyRedisKeyPrefix.STRATEGIES_STAT_DATA_MAP.fullKey(id + ""));
        if (statData != null){
            strategy.setViewnum((Integer) statData.get("viewnum"));
            strategy.setReplynum((Integer) statData.get("replynum"));
            strategy.setFavornum((Integer) statData.get("favornum"));
            strategy.setSharenum((Integer) statData.get("sharenum"));
            strategy.setThumbsupnum((Integer) statData.get("thumbsupnum"));
        }
        return strategy;
    }

    /**
     * 保存攻略对象
     * @param entity
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean save(Strategy entity) {
        return doSaveOrUpdate(entity);
    }

    /**
     * 更新攻略对象
     * @param entity
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateById(Strategy entity) {
        return doSaveOrUpdate(entity);
    }


    /**
     * 攻略分类分组
     * @param destId
     * @return
     */
    @Override
    public List<StrategyCatalog> findGroupByDestId(Long destId) {
        return getBaseMapper().selectGroupByDestId(destId);
    }

    /**
     * 攻略内容
     * @param id
     * @return
     */
    @Override
    public StrategyContent getContentById(Long id) {
        return strategyContentMapper.selectById(id);
    }

    /**
     * 目的地攻略 top 3查询
     * @param destId
     * @return
     */
    @Override
    public List<Strategy> findViewnumTop3ByDestId(Long destId) {
        // 查询指定目的地之下浏览数前三的攻略
        QueryWrapper<Strategy> wrapper = new QueryWrapper<Strategy>()
                .eq("dest_id", destId)
                .orderByDesc("viewnum")
                .last("limit 3");
        return list(wrapper);
    }

    /**
     * 按照目的地查询攻略
     * @param qo
     * @return
     */
    @Override
    public Page<Strategy> pageList(StrategyQuery qo) {

        if ((qo.getType() != null && qo.getType() != -1) && (qo.getRefid() != null && qo.getRefid() != -1)){
            if (qo.getType() == 3){
                // 如果当前类型 == 3 就按照主题查询
                qo.setThemeId(qo.getRefid());
            }else {
                // 否则按照目的地查询
                qo.setDestId(qo.getRefid());
            }
        }

        QueryWrapper<Strategy> wrapper = new QueryWrapper<Strategy>()
                .eq(qo.getDestId() != null, "dest_id", qo.getDestId())
                .eq(qo.getThemeId() != null, "theme_id", qo.getThemeId())
                .orderByDesc(!StringUtils.isEmpty(qo.getOrderBy()), qo.getOrderBy());
        return super.page(new Page<>(qo.getCurrent(), qo.getSize()), wrapper);
    }

    /**
     * 根据是否在国内进行攻略查询
     * @param abroad
     * @return
     */
    @Override
    public List<StrategyCondition> findDestCondition(int abroad) {
        return getBaseMapper().selectDestCondition(abroad);
    }

    /**
     * 根据攻略主题进行查询
     * @return
     */
    @Override
    public List<StrategyCondition> findThemeCondition() {
        return getBaseMapper().selectThemeCondition();
    }

    /**
     * 阅读数+1
     * @param id
     */
    @Override
    public void viewnumTncr(Long id) {
        this.statDataIncr("viewnum", id);
        // 记录操作次数
        redisCache.zsetIncrement(StrategyRedisKeyPrefix.STRATEGIES_STAT_COUNT_RANK_ZSET, 1, id);// 前缀，分数，成员
    }

    /**
     * 置顶数
     * @param sid
     * @return
     */
    @Override
    public boolean thumbnumIncr(Long sid) {
        LoginUser user = AuthenticationUtils.getUser();
        // 查询 redis 的记录，判断是否已经置顶过，如果有直接抛出异常
        StrategyRedisKeyPrefix keyPrefix = StrategyRedisKeyPrefix.STRATEGIES_TOP_MAP;
        String fullKey = keyPrefix.fullKey(sid+""); // 外部的 key
        // TODO 如果此时一个用户非常快速的访问了多次当前接口，在此处会不会被拦截
        // 使用Redis事务来保证操作的原子性

        Integer count = redisCache.getCacheMapValue(fullKey, user.getId() + "");
        if (count != null && count > 0){
            return false;
        }
        // 否则先记录当前用户已经针对该文章置顶，并设置过期时间为今天最后一秒
        // 从当前时间开始，到今天的最后一秒
        keyPrefix.setTimeout(DateUtils.getLastMillisSeconds());
        keyPrefix.setUnit(TimeUnit.SECONDS);
        Long ret = redisCache.hashIncrement(
                keyPrefix, user.getId() + "", 1, sid + ""
        );// 用户 +1
        if (ret > 0){
            // 说明之前已经存在过值
            return false;
        }
        // 文章置顶数+1
        this.statDataIncr("thumbnum", sid);
        // 记录操作次数
        redisCache.zsetIncrement(StrategyRedisKeyPrefix.STRATEGIES_STAT_COUNT_RANK_ZSET, 1, sid);// 前缀，分数，成员
        return true;
    }

    /**
     * 数据回显
     * @param id
     * @return
     */
    @Override
    public Map<String, Object> getStatData(Long id) {
        return redisCache.getCacheMap(StrategyRedisKeyPrefix.STRATEGIES_STAT_DATA_MAP.fullKey(id + ""));
    }


    private void statDataIncr(String hashKey, Long sid){
        redisCache.hashIncrement(StrategyRedisKeyPrefix.STRATEGIES_STAT_DATA_MAP, hashKey,1, sid+"");
        // 记录操作次数
        redisCache.zsetIncrement(StrategyRedisKeyPrefix.STRATEGIES_STAT_COUNT_RANK_ZSET, 1, sid);// 前缀，分数，成员

    }

    /**
     * 保存或者更新操作
     * @param entity
     * @return
     */
    private boolean doSaveOrUpdate(Strategy entity){
        // 1. 完成封面图片的上传，且得到 url 后重新设置到 cover 中
        if (!StringUtils.isEmpty(entity.getCoverUrl()) || entity.getCoverUrl().startsWith("http")){
            String fileName = UUID.randomUUID().toString();
            String url = OssUtil.uploadImgByBase64("images/strategies", fileName + ".jpg", entity.getCoverUrl());
            entity.setCoverUrl(url);
        }

        // 2. 补充分类名称
        StrategyCatalog catalog = strategyCatalogService.getById(entity.getCatalogId());
        entity.setCatalogName(catalog.getName());
        // 3. 根据分类中的目的地id/名称设置到攻略对象中
        entity.setCatalogId(catalog.getDestId());
        entity.setDestName(catalog.getDestName());
        // 基于目的地判断是否是国内
        List<Destination> toasts = destinationService.findToasts(catalog.getDestId());
        Destination dest = toasts.get(0);
        if (dest.getId() == 1){
            entity.setIsabroad(Strategy.ABROAD_NO);
        }else {
            entity.setIsabroad(Strategy.ABROAD_YES);
        }
        // 4. 查询主题，设置主题名称
        StrategyTheme theme = strategyThemeService.getById(entity.getThemeId());
        entity.setThemeName(theme.getName());

        // 判断是更新还是新增
        if (entity.getId() == null){
            // 5. 设置创建时间
            entity.setCreateTime(new Date());
            // 6. 设置各种数量为0\
            entity.setViewnum(0);
            entity.setSharenum(0);
            entity.setThumbsupnum(0);
            entity.setFavornum(0);
            entity.setReplynum(0);
            // 7. 重新设置默认状态，覆盖前端提交的值
            entity.setState(Strategy.STATE_NORMAL);
            // 8. 保存攻略对象，得到攻略自增的 id
            boolean save = super.save(entity);
            // 9. 将攻略 id 设置到攻略内容对象中，并保存内容对象
            StrategyContent content = entity.getContent();
            content.setId(entity.getId());
            return save && strategyContentMapper.insert(content) > 0;
        }

        // 更新操作
        boolean ret = super.updateById(entity);
        StrategyContent content = entity.getContent();
        content.setId(entity.getId());
        int row = strategyContentMapper.updateById(entity.getContent());
        return ret && row > 0;
    }
}
