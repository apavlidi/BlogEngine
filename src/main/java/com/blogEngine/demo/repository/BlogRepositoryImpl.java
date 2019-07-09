package com.blogEngine.demo.repository;

import com.blogEngine.demo.domain.Blog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

public class BlogRepositoryImpl implements BlogRepositoryCustom {

  @Autowired
  private MongoTemplate mongoTemplate;

  @Override
  public Blog saveBlog(Blog blog) {
    mongoTemplate.save(blog);
    return blog;
  }

  @Override
  public Blog deleteBlog(String title) {
    Query query = new Query();
    query.addCriteria(Criteria.where("title").is(title));
    return mongoTemplate.findAndRemove(query, Blog.class);
  }

  @Override
  public Blog findByTitle(String title) {
    Query query = new Query();
    query.addCriteria(Criteria.where("title").is(title));
    return mongoTemplate.findOne(query, Blog.class);
  }

}
