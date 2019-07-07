package com.blogEngine.demo.repository;

import com.blogEngine.demo.domain.Blog;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BlogRepository extends MongoRepository<Blog, Long>, BlogRepositoryCustom {

}
