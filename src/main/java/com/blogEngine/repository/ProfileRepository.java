package com.blogEngine.repository;

import com.blogEngine.domain.Profile;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProfileRepository extends MongoRepository<Profile, Long>, ProfileRepositoryCustom {

}
