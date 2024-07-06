package com.pzhu.comment.service.impl;

import com.pzhu.comment.domain.StrategyComment;
import com.pzhu.comment.qo.CommentQuery;
import com.pzhu.comment.repository.StrategyCommentRepository;
import com.pzhu.comment.service.StrategyCommentService;
import com.pzhu.core.qo.QueryObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StrategyCommentServiceImpl implements StrategyCommentService {

    private final StrategyCommentRepository strategyCommentRepository;
    private final MongoTemplate mongoTemplate;

    public StrategyCommentServiceImpl(StrategyCommentRepository strategyCommentRepository, MongoTemplate mongoTemplate) {
        this.strategyCommentRepository = strategyCommentRepository;
        this.mongoTemplate = mongoTemplate;
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
}
