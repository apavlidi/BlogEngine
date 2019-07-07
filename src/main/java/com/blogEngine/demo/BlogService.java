package com.blogEngine.demo;

import com.blogEngine.demo.domain.Blog;
import com.blogEngine.demo.repository.BlogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BlogService {

  @Autowired
  private BlogRepository blogRepository;

  public Blog getBlogByTitle(String title) {
    Blog blog = blogRepository.findByTitle(title);
    if (blog == null) {
      throw new BlogNotFoundException();
    }
    return blog;
  }
}
