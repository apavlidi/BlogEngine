package com.blogEngine.service;

import com.blogEngine.domain.Blog;
import com.blogEngine.repository.BlogRepository;
import com.blogEngine.restExceptions.BlogNotFoundException;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class BlogService {

  private final BlogRepository blogRepository;

  public BlogService(BlogRepository blogRepository) {
    this.blogRepository = blogRepository;
  }

  public Blog getBlogByTitle(String title) {
    Blog blog = blogRepository.findByTitle(title);
    if (blog == null) {
      throw new BlogNotFoundException();
    }
    return blog;
  }

  public Blog saveBlog(Blog blog) {
    return blogRepository.saveBlog(blog);
  }

  public Blog deleteBlog(String blogTitle) {
    return blogRepository.deleteBlog(blogTitle);
  }

  public Blog editBlogByTitle(Blog newBlog, String title) {
    return blogRepository.updateBlog(title, newBlog);
  }

  public List<Blog> getBlogs() {
    return blogRepository.getBlogs();
  }

  public List<Blog> getBlogs(Map<String, String> restApiQueries) {
    return blogRepository.getBlogs(restApiQueries);
  }
}
