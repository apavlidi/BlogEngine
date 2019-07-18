package com.blogEngine.domain;

import java.util.List;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "profile")
public class Profile {

  @Id
  @Null
  private String id;

  @NotNull
  @Size(min = 4, message = "Username is required")
  private String username;

  private String firstName;

  private String lastName;

  private List<Blog> blogs;

  public Profile() {
  }

  public Profile(String username) {
    this.username = username;
  }

  public List<Blog> getBlogs() {
    return blogs;
  }

  public void setBlogs(List<Blog> blogs) {
    this.blogs = blogs;
  }

  public String getId() {
    return id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }
}
