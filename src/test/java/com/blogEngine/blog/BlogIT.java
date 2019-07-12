package com.blogEngine.blog;

import static com.blogEngine.blog.config.DatabaseProfiles.TEST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.OK;

import com.blogEngine.blog.domain.Blog;
import com.blogEngine.blog.repository.BlogRepository;
import java.time.LocalDate;
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
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT) //it boots a server
@ActiveProfiles(TEST)
public class BlogIT {

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

    assertThat(response.getStatusCode()).isEqualTo(OK);
    assertThat(response.getBody().getText()).isEqualTo("test");
    assertThat(response.getBody().getDate()).isEqualTo(LocalDate.now().toString());
  }

}
