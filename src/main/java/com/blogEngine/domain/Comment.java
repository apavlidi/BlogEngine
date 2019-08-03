package com.blogEngine.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Document(collection = "comment")
public class Comment {

    @Id
    private String id;

    @NotNull
    private Blog blog;

    private List<Comment> comments;

    @Size(min = 1, message = "Text should have at least 1 character")
    private String text;

    public Comment() {
    }

    public Comment(String commentId) {
        this.id = commentId;
    }

    public String getId() {
        return id;
    }

    public Blog getBlog() {
        return blog;
    }

    public void setBlog(Blog blog) {
        this.blog = blog;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
