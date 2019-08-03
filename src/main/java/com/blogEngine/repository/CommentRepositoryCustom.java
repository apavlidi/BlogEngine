package com.blogEngine.repository;

import com.blogEngine.domain.Comment;

import java.util.List;

public interface CommentRepositoryCustom {
    Comment getCommentById(String commentId);

    Comment saveComment(Comment comment);

    List<Comment> getComments();

    Comment deleteById(String commentId);

    Comment update(String commentId, Comment newComment);
}
