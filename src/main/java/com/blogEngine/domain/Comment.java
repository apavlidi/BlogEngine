package com.blogEngine.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Null;
import java.util.List;

@Document(collection = "comment")
public class Comment {

    @Id
    private String id;

    private Blog blog;

    private List<Comment> comments;

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
}
