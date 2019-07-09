package com.blogEngine.demo.domain;

import java.time.LocalDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "blogs")
public class Blog {

  @Id
  private String id;

  private String title;

  private String text;

  private LocalDate date;

  public Blog() {
  }

  public Blog(String title) {
    this.title = title;
  }

  public Blog(String text, LocalDate date) {
    this.text = text;
    this.date = date;
  }

  public Blog(String title,String text, LocalDate date) {
    this.title = title;
    this.text = text;
    this.date = date;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public LocalDate getDate() {
    return date;
  }

  public void setDate(LocalDate date) {
    this.date = date;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }
}
