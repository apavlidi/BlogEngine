package com.blogEngine.config;

import com.mongodb.MongoClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
@Profile(DatabaseProfiles.DEV)
public class MongoConfig {

  @Bean
  public MongoClient mongo() {
    return new MongoClient("localhost");
  }

  @Bean
  public MongoTemplate mongoTemplate() {
    return new MongoTemplate(mongo(), "blogEngine");
  }

}