package com.pzhu.comment.service;

import com.pzhu.comment.domain.TravelComment;
import com.pzhu.comment.qo.CommentQuery;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TravelCommentService {

    Page<TravelComment> page(CommentQuery qo);

    void save(TravelComment comment);

    List<TravelComment> findList(Long travelId);
}
