package com.blogEngine.repository;

import com.blogEngine.domain.Profile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

public class ProfileRepositoryImpl implements ProfileRepositoryCustom {

  @Autowired
  private MongoTemplate mongoTemplate;

  @Override
  public Profile findByUsername(String username) {
    Query query = new Query();
    query.addCriteria(Criteria.where("username").is(username));
    return mongoTemplate.findOne(query, Profile.class);
  }

  @Override
  public Profile saveProfile(Profile profile) {
    return mongoTemplate.save(profile);
  }

  @Override
  public Profile deleteProfile(String username) {
    return null;
  }
}
