package com.pzhu.comment.service.impl;

import com.pzhu.auth.util.AuthenticationUtils;
import com.pzhu.comment.domain.StrategyComment;
import com.pzhu.comment.qo.CommentQuery;
import com.pzhu.comment.redis.key.CommentRedisKeyPrefix;
import com.pzhu.comment.repository.StrategyCommentRepository;
import com.pzhu.comment.service.StrategyCommentService;
import com.pzhu.redis.utils.RedisCache;
import com.pzhu.user.vo.LoginUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class StrategyCommentServiceImpl implements StrategyCommentService {

    private final StrategyCommentRepository strategyCommentRepository;
    private final MongoTemplate mongoTemplate;
    private final RedisCache redisCache;

    public StrategyCommentServiceImpl(StrategyCommentRepository strategyCommentRepository, MongoTemplate mongoTemplate, RedisCache redisCache) {
        this.strategyCommentRepository = strategyCommentRepository;
        this.mongoTemplate = mongoTemplate;
        this.redisCache = redisCache;
    }


    /**
     * 分页查询
     * @param qo
     * @return
     */
    @Override
    public Page<StrategyComment> page(CommentQuery qo) {
        // 1. 拼接查询条件
        Criteria criteria = Criteria.where("strategyId").is(qo.getArticleId());
        // 2. 创还能查询条件，关联条件
        Query query = new Query();
        query.addCriteria(criteria);

        // 统计总数
        long total = mongoTemplate.count(query, StrategyComment.class);
        if (total == 0){
            return Page.empty();
        }

        // 3. 设置分页参数
        /*PageRequest request = PageRequest.of(qo.getCurrent() - 1, qo.getSize());
        query.with(request);*/ // 不方便
        PageRequest request = PageRequest.of(qo.getCurrent() - 1, qo.getSize());
        query.skip(request.getOffset()).limit(request.getPageSize());
        // 4. 按照时间排序
        query.with(Sort.by(Sort.Direction.DESC, "createTime"));
        // 5. 分页查询
        List<StrategyComment> list = mongoTemplate.find(query, StrategyComment.class);

        return new PageImpl<>(list, request, total);
    }

    /**
     * 发布评论
     * @param comment
     */
    @Override
    public void save(StrategyComment comment) {
        // 获取当前登录的用户
        LoginUser user = AuthenticationUtils.getUser();
        comment.setUserId(user.getId());
        comment.setNickname(user.getNickname());
        comment.setCity(user.getCity());
        comment.setLevel(user.getLevel());
        comment.setHeadImgUrl(user.getHeadImgUrl());
        comment.setCreateTime(new Date());
        // 可以使用 copy

        // 保存到mongodb
        strategyCommentRepository.save(comment);
    }

    /**
     * 点赞
     * @param cid
     */
    @Override
    public void doLike(String cid) {
        // 基于 cid 查询评论对象
        Optional<StrategyComment> optional = strategyCommentRepository.findById(cid);
        if (optional.isPresent()){ // 判断 optional 是否为 null
            StrategyComment strategyComment = optional.get();
            // 获取当前登录的用户对象
            LoginUser user = AuthenticationUtils.getUser();
            // 判断当前用户是否已经点过赞
            if (strategyComment.getThumbuplist().contains(user.getId())) {
                // 如果点过赞，点赞数-1，将用户 id 从集合中删除
                strategyComment.setThumbupnum(strategyComment.getThumbupnum() - 1);
                strategyComment.getThumbuplist().remove(user.getId());
            }else {
                // 如果没点赞，点赞书+1，将用户 id 添加到集合中
                strategyComment.setThumbupnum(strategyComment.getThumbupnum() + 1);
                strategyComment.getThumbuplist().add(user.getId());
            }

            // 重新将对象保存到 mongodb
            strategyCommentRepository.save(strategyComment);
        }
    }

    /**
     * 评论数+1
     * @param strategyId
     */
    @Override
    public void replyNumIncr(Long strategyId) {
        redisCache.hashIncrement(CommentRedisKeyPrefix.STRATEGIES_STAT_DATA_MAP, "replynum", 1, strategyId+"");
    }


}
