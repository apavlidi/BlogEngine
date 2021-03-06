package com.blogEngine.repository;

import com.blogEngine.domain.Profile;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndReplaceOptions;
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
    Query query = new Query();
    query.addCriteria(Criteria.where("username").is(username));
    return mongoTemplate.findAndRemove(query, Profile.class);
  }

  @Override
  public Profile updateProfile(String username, Profile newProfile) {
    Query query = new Query();
    query.addCriteria(Criteria.where("username").is(username));
    return mongoTemplate
        .findAndReplace(query, newProfile, FindAndReplaceOptions.options().returnNew());
  }

  @Override
  public List<Profile> getAllProfiles() {
    return mongoTemplate.findAll(Profile.class);
  }
}
