package com.blogEngine.controller;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import com.blogEngine.domain.Blog;
import com.blogEngine.restExceptions.BlogNotFoundException;
import com.blogEngine.restExceptions.WrongQueryParam;
import com.blogEngine.service.BlogService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
  private Blog postBlog(@Valid @RequestBody Blog blog) {
    return blogService.saveBlog(blog);
  }

  @DeleteMapping("/{blogTitle}")
  private Blog deleteBlog(@PathVariable String blogTitle) {
    return blogService.deleteBlog(blogTitle);
  }

  @PutMapping(value = "/{blogTitle}", produces = {MediaType.APPLICATION_JSON_VALUE})
  private Blog putBlog(@Valid @RequestBody Blog newBlog, @PathVariable String blogTitle) {
    return blogService.editBlogByTitle(newBlog, blogTitle);
  }

  @ExceptionHandler
  @ResponseStatus(NOT_FOUND)
  private void blogNotFoundException(BlogNotFoundException ex) {
  }

  @ExceptionHandler
  @ResponseStatus(BAD_REQUEST)
  private void wrongQueryParam(WrongQueryParam ex) {
  }

  @ExceptionHandler
  @ResponseStatus(BAD_REQUEST)
  private void invalidArgs(MethodArgumentNotValidException ex) {
  }

  @GetMapping("/demo")
  private List<Blog> getBlogs(
      @RequestParam(name = "sort", required = false) String sortValue,
      @RequestParam(name = "select", required = false) String selectValue,
      @RequestParam(name = "pageSize", required = false) String pageSizeValue,
      @RequestParam(name = "page", required = false) String pageValue,
      @RequestParam(name = "q", required = false) String searchValue) {
    Map<String, String> restApiQueries = collectRestApiParams(sortValue, searchValue, selectValue, pageValue, pageSizeValue);
    return blogService.getBlogs(restApiQueries);
  }

  private Map<String, String> collectRestApiParams(String sortValue, String searchValue, String selectFieldValue, String pageValue, String pageSizeValue) {
    Map<String, String> restApiQueries = new HashMap<>();

    if (sortValue != null) {
      restApiQueries.put("sort", sortValue);
    }
    if (pageValue != null) {
      restApiQueries.put("page", pageValue);
    }
    if (pageSizeValue != null) {
      restApiQueries.put("pageSize", pageSizeValue);
    }
    if (searchValue != null) {
      restApiQueries.put("q", searchValue);
    }
    if (selectFieldValue != null) {
      restApiQueries.put("select", selectFieldValue);
    }

    return restApiQueries;
  }

}
