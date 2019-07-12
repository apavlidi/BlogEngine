package com.blogEngine.blog.repository;

import com.blogEngine.blog.domain.Blog;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BlogRepository extends MongoRepository<Blog, Long>, BlogRepositoryCustom {

}
