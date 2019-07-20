package com.blogEngine.domain;

import java.util.Calendar;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "blogs")
public class Blog {

  @Id
  @Null
  private String id;

  @NotNull
  @Size(min = 2, message = "Title should have at least 2 characters")
  private String title;

  @NotNull
  @Size(min = 1, message = "Text should have at least 1 character")
  private String text;

  @CreatedDate
  private Calendar date;

  @NotNull
  @Valid
  private Profile profile;

  public Blog() {
  }

  public Blog(String title) {
    this.title = title;
  }

  public Blog(String text, Calendar date) {
    this.text = text;
    this.date = date;
  }

  public Blog(String title, String text, Calendar date) {
    this.title = title;
    this.text = text;
    this.date = date;
  }

  public Profile getProfile() {
    return profile;
  }

  public void setProfile(Profile profile) {
    this.profile = profile;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public Calendar getDate() {
    return date;
  }

  public void setDate(Calendar date) {
    this.date = date;
  }

  public String getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }
}
