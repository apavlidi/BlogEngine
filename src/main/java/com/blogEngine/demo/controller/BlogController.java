package com.blogEngine.demo.controller;

import static org.springframework.http.HttpStatus.NOT_FOUND;

import com.blogEngine.demo.BlogNotFoundException;
import com.blogEngine.demo.BlogService;
import com.blogEngine.demo.domain.Blog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BlogController {

  @Autowired
  private BlogService blogService;

  @GetMapping("/blogs/{blogTitle}")
  private Blog getBlog(@PathVariable String blogTitle) {
    return blogService.getBlogByTitle(blogTitle);
  }

  @ExceptionHandler
  @ResponseStatus(NOT_FOUND)
  private void blogNotFoundException(BlogNotFoundException ex) {
  }


}
