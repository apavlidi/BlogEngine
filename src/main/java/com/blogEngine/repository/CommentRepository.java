package com.blogEngine.repository;

import com.blogEngine.domain.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CommentRepository extends MongoRepository<Comment, Long>, CommentRepositoryCustom {

}
