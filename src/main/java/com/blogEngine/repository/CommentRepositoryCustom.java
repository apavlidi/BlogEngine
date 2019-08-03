package com.blogEngine.repository;

import com.blogEngine.domain.Comment;

public interface CommentRepositoryCustom {
    Comment getCommentById(String commentId);
}
