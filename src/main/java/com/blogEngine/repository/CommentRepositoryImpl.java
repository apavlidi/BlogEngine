package com.blogEngine.repository;

import com.blogEngine.domain.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndReplaceOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

public class CommentRepositoryImpl implements CommentRepositoryCustom {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public Comment findCommentBy(String commentId) {
        return mongoTemplate.findById(commentId, Comment.class);
    }

    @Override
    public Comment saveComment(Comment comment) {
        return mongoTemplate.save(comment);
    }

    @Override
    public Comment deleteBy(String commentId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(commentId));
        return mongoTemplate.findAndRemove(query, Comment.class);
    }

    @Override
    public Comment update(String commentId, Comment newComment) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(commentId));
        return mongoTemplate.findAndReplace(query, newComment, FindAndReplaceOptions.options().returnNew());
    }
}
