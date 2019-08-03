package com.blogEngine.controller;

import com.blogEngine.domain.Comment;
import com.blogEngine.restExceptions.CommentNotFoundException;
import com.blogEngine.service.CommentService;
import org.springframework.data.mapping.MappingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping(value = "/{commentId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public Comment getComment(@PathVariable String commentId) {
        return commentService.getCommentById(commentId);
    }

    @GetMapping
    public List<Comment> getComments() {
        return commentService.getComments();
    }

    @PostMapping
    public Comment postComment(@Valid @RequestBody Comment comment) {
        return commentService.saveComment(comment);
    }

    @PutMapping(value = "/{commentId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public Comment postComment(@Valid @RequestBody Comment comment, @PathVariable String commentId) {
        return commentService.updateComment(commentId, comment);
    }

    @DeleteMapping("/{commentId}")
    public Comment deleteComment(@PathVariable String commentId) {
        return commentService.delete(commentId);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    private void commentNotFound(CommentNotFoundException e) {
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private void commentIsInvalid(MappingException e) {
    }

}
