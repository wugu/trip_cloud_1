package com.pzhu.comment.repository;

import com.pzhu.comment.domain.TravelComment;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TravelCommentRepository extends MongoRepository<TravelComment,String> {
}
