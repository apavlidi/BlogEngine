package com.blogEngine.repository;

import com.blogEngine.domain.Blog;
import com.blogEngine.restExceptions.WrongQueryParam;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
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

  @Override
  public List<Blog> getBlogs(Map<String, String> restApiQueries) {
    Query query = new Query();
    applyRestApiQueries(query, restApiQueries);
    return mongoTemplate.find(query, Blog.class);
  }

  private void applyRestApiQueries(Query query, Map<String, String> restApiQueries) {
    applySortQueryParam(query, restApiQueries);
    applySelectQueryParam(query, restApiQueries);
    applySearchQueryParam(query, restApiQueries);
    applyPageQueryParam(query, restApiQueries);
    applyPageSizeQueryParam(query, restApiQueries);
  }

  private void applyPageQueryParam(Query query, Map<String, String> restApiQueries) {
    try {
      if (restApiQueries.get("page") != null) {
        String pageQueryParam = restApiQueries.get("page");
        query.skip(Long.parseLong(pageQueryParam));
      }
    } catch (NumberFormatException pageParamExc) {
      throw new WrongQueryParam();
    }
  }

  private void applyPageSizeQueryParam(Query query, Map<String, String> restApiQueries) {
    try {
      if (restApiQueries.get("pageSize") != null) {
        String pageSizeQueryParam = restApiQueries.get("pageSize");
        query.limit(Integer.parseInt(pageSizeQueryParam));
      }
    } catch (NumberFormatException pageParamExc) {
      throw new WrongQueryParam();
    }
  }

  private void applySearchQueryParam(Query query, Map<String, String> restApiQueries) {
    try {
      if (restApiQueries.get("q") != null) {
        JSONObject jsonCriteria = new JSONObject(restApiQueries.get("q"));
        JSONArray keys = jsonCriteria.names();
        for (int i = 0; i < keys.length(); ++i) {
          String field = keys.getString(i);
          String value = jsonCriteria.getString(field);
          query.addCriteria(Criteria.where(field).is(value));
        }
      }
    } catch (JSONException e) {
      throw new WrongQueryParam();
    }
  }

  private void applySelectQueryParam(Query query, Map<String, String> restApiQueries) {
    if (restApiQueries.get("select") != null) {
      String[] selectedFields = restApiQueries.get("select").split(",");
      for (String selectedField : selectedFields) {
        query.fields().include(selectedField);
      }
    }
  }

  private void applySortQueryParam(Query query, Map<String, String> restApiQueries) {
    try {
      if (restApiQueries.get("sort") != null) {
        String sortQueryParam = restApiQueries.get("sort");
        Direction sortDir =
            sortQueryParam.substring(0, 1).equals("-") ? Direction.ASC : Direction.DESC;
        String sortBy = sortQueryParam.substring(1);
        query.with(new Sort(sortDir, sortBy));
      }
    } catch (IndexOutOfBoundsException subStringException) {
      throw new WrongQueryParam();
    }
  }

}
