package com.blogEngine.repository;

import com.blogEngine.domain.Blog;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.FindAndReplaceOptions;
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
  public List<Blog> getBlogs() {
    return mongoTemplate.findAll(Blog.class);
  }

  @Override
  public List<Blog> getBlogs(Map<String, String> restApiQueries) {
    Query query = new Query();
    query.with(new Sort(Sort.Direction.ASC, "date"));
    return mongoTemplate.find(query, Blog.class);
  }

  @Override
  public Blog updateBlog(String title, Blog newBlog) {
    Query query = new Query();
    query.addCriteria(Criteria.where("title").is(title));
    return mongoTemplate
        .findAndReplace(query, newBlog, FindAndReplaceOptions.options().returnNew());
  }

  @Override
  public Blog findByTitle(String title) {
    Query query = new Query();
    query.addCriteria(Criteria.where("title").is(title));
    return mongoTemplate.findOne(query, Blog.class);
  }

}
