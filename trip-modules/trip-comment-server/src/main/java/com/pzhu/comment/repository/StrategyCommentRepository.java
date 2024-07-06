package com.pzhu.comment.repository;

import com.pzhu.comment.domain.StrategyComment;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StrategyCommentRepository extends MongoRepository<StrategyComment,String> {
}
