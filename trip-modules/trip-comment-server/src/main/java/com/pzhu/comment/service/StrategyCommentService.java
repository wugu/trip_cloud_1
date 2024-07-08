package com.pzhu.comment.service;

import com.pzhu.comment.domain.StrategyComment;
import com.pzhu.comment.qo.CommentQuery;
import org.springframework.data.domain.Page;

public interface StrategyCommentService {

    Page<StrategyComment> page(CommentQuery qo);

    void save(StrategyComment comment);

    void doLike(String cid);

    void replyNumIncr(Long strategyId);

}
