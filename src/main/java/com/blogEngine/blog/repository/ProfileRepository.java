package com.blogEngine.blog.repository;

import com.blogEngine.blog.domain.Profile;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProfileRepository extends MongoRepository<Profile, Long>, ProfileRepositoryCustom {

}
