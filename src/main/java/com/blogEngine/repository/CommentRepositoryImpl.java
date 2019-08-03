package com.blogEngine.repository;

import com.blogEngine.domain.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.List;

public class CommentRepositoryImpl implements CommentRepositoryCustom {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public Comment findCommentBy(String commentId) {
        return mongoTemplate.findById(commentId, Comment.class);
    }

    @Override
    public Comment saveComment(Comment comment) {
        return null;
    }

    @Override
    public List<Comment> findAllComments() {
        return null;
    }

    @Override
    public Comment deleteBy(String commentId) {
        return null;
    }

    @Override
    public Comment update(String commentId, Comment newComment) {
        return null;
    }
}
