package com.blogEngine.service;

import com.blogEngine.domain.Comment;
import com.blogEngine.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    public Comment getCommentById(String commentId) {
        return commentRepository.getCommentById(commentId);
    }

    public List<Comment> getComments() {
        return null;
    }

    public Comment saveComment(Comment comment) {
        return null;
    }

    public Comment delete(String commentId) {
        return null;
    }

    public Comment updateComment(String commentId, Comment updatedComment) {
        return null;
    }
}
