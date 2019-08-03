package com.blogEngine.repository;

import com.blogEngine.domain.Comment;

import java.util.List;

public interface CommentRepositoryCustom {

    Comment findCommentBy(String commentId);

    Comment saveComment(Comment comment);

    List<Comment> findAllComments();

    Comment deleteBy(String commentId);

    Comment update(String commentId, Comment newComment);
}
