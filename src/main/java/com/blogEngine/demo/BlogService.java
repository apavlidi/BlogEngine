package com.blogEngine.demo;

import com.blogEngine.demo.domain.Blog;
import com.blogEngine.demo.repository.BlogRepository;
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
