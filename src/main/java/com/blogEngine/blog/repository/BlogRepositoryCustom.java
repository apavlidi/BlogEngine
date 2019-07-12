package com.blogEngine.blog.repository;

import com.blogEngine.blog.domain.Blog;


public interface BlogRepositoryCustom {

  Blog findByTitle(String title);

  Blog saveBlog(Blog blog);

  Blog deleteBlog(String title);

}