package com.blogEngine.demo;

import static org.springframework.http.HttpStatus.OK;

import com.blogEngine.demo.domain.Blog;
import com.blogEngine.demo.repository.BlogRepository;
import java.time.LocalDate;
import org.assertj.core.api.Assertions;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class BlogEngineIT {

  @Rule
  public final MongoCleanupRule cleanupRule = new MongoCleanupRule(this, Blog.class);

  @Autowired
  private TestRestTemplate restTemplate;

  @Autowired
  private BlogRepository blogRepository;

  @Autowired
  private MongoTemplate mongoTemplate;

  @Test
  public void getBlog_returnBlogDetails() {
    Blog blog = new Blog("title");
    blog.setDate(LocalDate.now());
    blog.setText("test");
    blogRepository.saveBlog(blog);

    ResponseEntity<Blog> response = restTemplate.getForEntity("/blogs/title", Blog.class);

    Assertions.assertThat(response.getStatusCode()).isEqualTo(OK);
    Assertions.assertThat(response.getBody().getText()).isEqualTo("test");
    Assertions.assertThat(response.getBody().getDate()).isEqualTo(LocalDate.now().toString());
  }
}
