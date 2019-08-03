package com.blogEngine.service;

import com.blogEngine.domain.Comment;
import com.blogEngine.repository.CommentRepository;
import com.blogEngine.restExceptions.CommentNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public Comment getCommentById(String commentId) {
        Comment comment = commentRepository.findCommentBy(commentId);
        if (comment == null) {
            throw new CommentNotFoundException();
        }
        return comment;
    }

    public List<Comment> getComments() {
        return commentRepository.findAll();
    }

    public Comment saveComment(Comment comment) {
        return commentRepository.saveComment(comment);
    }

    public Comment delete(String commentId) {
        Comment comment = commentRepository.deleteBy(commentId);
        if (comment == null) {
            throw new CommentNotFoundException();
        }
        return comment;
    }

    public Comment updateComment(String commentId, Comment newComment) {
        Comment updatedComment = commentRepository.update(commentId, newComment);
        if (updatedComment == null) {
            throw new CommentNotFoundException();
        }
        return updatedComment;
    }
}
