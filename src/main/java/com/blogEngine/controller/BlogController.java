package com.blogEngine.controller;

import static org.springframework.http.HttpStatus.NOT_FOUND;

import com.blogEngine.domain.Blog;
import com.blogEngine.restExceptions.BlogNotFoundException;
import com.blogEngine.service.BlogService;
import java.util.List;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "blogs")
public class BlogController {

  private final BlogService blogService;

  public BlogController(BlogService blogService) {
    this.blogService = blogService;
  }

  @GetMapping
  private List<Blog> getBlogs() {
    return blogService.getBlogs();
  }

  @GetMapping("/{blogTitle}")
  private Blog getBlog(@PathVariable String blogTitle) {
    return blogService.getBlogByTitle(blogTitle);
  }

  @PostMapping
  private String postBlog(@Valid @RequestBody Blog blog) {
    blogService.saveBlog(blog);
    return HttpStatus.OK.toString();
  }

  @DeleteMapping("/{blogTitle}")
  private String deleteBlog(@PathVariable String blogTitle) {
    blogService.deleteBlog(blogTitle);
    return HttpStatus.OK.toString();
  }

  @PutMapping("/{blogTitle}")
  private String putBlog(@RequestBody Blog newBlog, @PathVariable String blogTitle) {
    blogService.editBlogByTitle(newBlog, blogTitle);
    return HttpStatus.OK.toString();
  }

  @ExceptionHandler
  @ResponseStatus(NOT_FOUND)
  private void blogNotFoundException(BlogNotFoundException ex) {
  }


}
