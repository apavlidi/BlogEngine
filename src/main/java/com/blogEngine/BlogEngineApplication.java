package com.blogEngine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableMongoAuditing
@EnableSwagger2
public class BlogEngineApplication {

  public static void main(String[] args) {
    SpringApplication.run(BlogEngineApplication.class, args);
  }

}
