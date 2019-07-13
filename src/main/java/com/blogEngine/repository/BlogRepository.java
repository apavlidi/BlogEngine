package com.blogEngine.repository;

import com.blogEngine.domain.Blog;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BlogRepository extends MongoRepository<Blog, Long>, BlogRepositoryCustom {

}
