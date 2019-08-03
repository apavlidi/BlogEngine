package com.blogEngine.repository;

import com.blogEngine.domain.Comment;

public interface CommentRepositoryCustom {

    Comment findCommentBy(String commentId);

    Comment saveComment(Comment comment);

    Comment deleteBy(String commentId);

    Comment update(String commentId, Comment newComment);
}
