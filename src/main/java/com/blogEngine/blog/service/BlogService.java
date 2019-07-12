package com.blogEngine.blog.service;

import com.blogEngine.blog.domain.Blog;
import com.blogEngine.blog.repository.BlogRepository;
import com.blogEngine.blog.restExceptions.BlogNotFoundException;
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
    Blog blog = blogRepository.findByTitle(title);
    blog.setDate(newBlog.getDate());
    blog.setTitle(newBlog.getTitle());
    return blogRepository.saveBlog(blog);
  }
}
