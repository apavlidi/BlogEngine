package com.blogEngine.demo.repository;

import com.blogEngine.demo.domain.Blog;


public interface BlogRepositoryCustom {

  Blog findByTitle(String title);

  Blog saveBlog(Blog blog);

  Blog deleteBlog(String title);

}