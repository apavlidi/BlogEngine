package com.blogEngine.service;

import com.blogEngine.domain.Comment;
import com.blogEngine.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    public Comment getCommentById(String commentId) {
        return commentRepository.getCommentById(commentId);
    }

}
