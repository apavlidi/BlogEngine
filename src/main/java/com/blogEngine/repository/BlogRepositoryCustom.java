package com.blogEngine.repository;

import com.blogEngine.domain.Blog;
import java.util.List;
import java.util.Map;


public interface BlogRepositoryCustom {

  Blog findByTitle(String title);

  Blog saveBlog(Blog blog);

  Blog deleteBlog(String title);

  List<Blog> getBlogs();

  List<Blog> getBlogs(Map<String, String> restApiQueries);

  Blog updateBlog(String title, Blog newBlog);
}