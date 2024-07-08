package com.pzhu.comment.service.impl;

import com.pzhu.auth.anno.RequireLogin;
import com.pzhu.auth.util.AuthenticationUtils;
import com.pzhu.comment.domain.TravelComment;
import com.pzhu.comment.qo.CommentQuery;
import com.pzhu.comment.repository.TravelCommentRepository;
import com.pzhu.comment.service.TravelCommentService;
import com.pzhu.user.vo.LoginUser;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

@Service
public class TravelCommentServiceImpl implements TravelCommentService {

    private final TravelCommentRepository travelCommentRepository;
    private final MongoTemplate mongoTemplate;

    public TravelCommentServiceImpl(TravelCommentRepository travelCommentRepository, MongoTemplate mongoTemplate) {
        this.travelCommentRepository = travelCommentRepository;
        this.mongoTemplate = mongoTemplate;
    }


    /**
     * 分页查询
     * @param qo
     * @return
     */
    @Override
    public Page<TravelComment> page(CommentQuery qo) {
        // 1. 拼接查询条件
        Criteria criteria = Criteria.where("" +
                "").is(qo.getArticleId());
        // 2. 创还能查询条件，关联条件
        Query query = new Query();
        query.addCriteria(criteria);
        
        // 统计总数
        long total = mongoTemplate.count(query, TravelComment.class);
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
        List<TravelComment> list = mongoTemplate.find(query, TravelComment.class);
        
        return new PageImpl<>(list, request, total);
    }

    @RequireLogin
    @Override
    public void save(TravelComment comment) {
        // 获取用户信息
        LoginUser user = AuthenticationUtils.getUser();
        // 补充前端没有传的参数
        comment.setUserId(user.getId());
        comment.setNickname(user.getNickname());
        comment.setCity(user.getCity());
        comment.setLevel(user.getLevel());
        comment.setHeadImgUrl(user.getHeadImgUrl());
        comment.setCreateTime(new Date());

        // 设置类型
        if (comment.getRefComment() != null && StringUtils.hasLength(comment.getRefComment().getId())){
            // 评论的评论
            comment.setType(TravelComment.TRAVLE_COMMENT_TYPE);
        } else {
            // 普通评论
            comment.setType(TravelComment.TRAVLE_COMMENT_TYPE_COMMENT);
        }

        // 保存评论
        travelCommentRepository.save(comment);
    }
}
