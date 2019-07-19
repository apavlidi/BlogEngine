package com.blogEngine.repository;

import com.blogEngine.domain.Blog;
import java.util.List;


public interface BlogRepositoryCustom {

  Blog findByTitle(String title);

  Blog saveBlog(Blog blog);

  Blog deleteBlog(String title);

  List<Blog> getBlogs();
}