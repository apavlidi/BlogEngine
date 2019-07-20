package com.blogEngine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
public class BlogEngineApplication {

  public static void main(String[] args) {
    SpringApplication.run(BlogEngineApplication.class, args);
  }

}
