package com.pzhu.article.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pzhu.article.domain.*;
import com.pzhu.article.mapper.StrategyContentMapper;
import com.pzhu.article.mapper.StrategyMapper;
import com.pzhu.article.qo.StrategyQuery;
import com.pzhu.article.service.DestinationService;
import com.pzhu.article.service.StrategyCatalogService;
import com.pzhu.article.service.StrategyService;
import com.pzhu.article.service.StrategyThemeService;
import com.pzhu.article.utils.OssUtil;
import com.pzhu.article.vo.StrategyCondition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;


@Service
public class StrategyServiceImpl extends ServiceImpl<StrategyMapper, Strategy> implements StrategyService {
    private final StrategyThemeService strategyThemeService;
    private final StrategyCatalogService strategyCatalogService;
    private final DestinationService destinationService;
    private final StrategyContentMapper strategyContentMapper;

    public StrategyServiceImpl(DestinationService destinationService, StrategyCatalogService strategyCatalogService, StrategyThemeService strategyThemeService, StrategyContentMapper strategyContentMapper) {
        this.destinationService = destinationService;
        this.strategyCatalogService = strategyCatalogService;
        this.strategyThemeService = strategyThemeService;
        this.strategyContentMapper = strategyContentMapper;
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
