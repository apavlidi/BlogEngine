package com.blogEngine.service;

import com.blogEngine.domain.Comment;
import com.blogEngine.repository.CommentRepository;
import com.blogEngine.restExceptions.CommentNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    public Comment getCommentById(String commentId) {
        Comment comment = commentRepository.getCommentById(commentId);
        if (comment == null) {
            throw new CommentNotFoundException();
        }
        return comment;
    }

    public List<Comment> getComments() {
        return commentRepository.getComments();
    }

    public Comment saveComment(Comment comment) {
        return commentRepository.saveComment(comment);
    }

    public Comment delete(String commentId) {
        Comment comment = commentRepository.deleteById(commentId);
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
