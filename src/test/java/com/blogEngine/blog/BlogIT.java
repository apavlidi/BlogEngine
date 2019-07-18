package com.blogEngine.blog;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import com.blogEngine.MongoCleanupRule;
import com.blogEngine.config.DatabaseProfiles;
import com.blogEngine.domain.Blog;
import com.blogEngine.domain.Profile;
import com.blogEngine.repository.BlogRepository;
import java.time.LocalDate;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT) //it boots a server
@ActiveProfiles(DatabaseProfiles.TEST)
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

  @Test
  public void postBlogWithCustomId_shouldReturnBadRequest() throws JSONException {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(APPLICATION_JSON);
    JSONObject blogJsonObject = new JSONObject();
    blogJsonObject.put("id", "custom_id");
    blogJsonObject.put("title", "Test Title");
    blogJsonObject.put("text", "Test Text");

    JSONObject profileJsonObject = new JSONObject();
    profileJsonObject.put("username", "alexis");

    blogJsonObject.put("profile", profileJsonObject);

    HttpEntity<String> request = new HttpEntity<>(blogJsonObject.toString(), headers);
    ResponseEntity<String> response = restTemplate
        .postForEntity("/blogs", request, String.class);

    assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);
  }

  @Test
  public void postBlogWithInvalidProfile_shouldReturnBadRequest() {
    //setup
    Blog invalidBlog = new Blog("Some valid title");
    invalidBlog.setText("Some valid text");
    invalidBlog.setProfile(new Profile());

    //act
    ResponseEntity<String> response = restTemplate.postForEntity("/blogs", invalidBlog, String.class);

    //assertions
    assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);
  }

  @Test
  public void postBlogWithInvalidTitle_shouldReturnBadRequest() {
    //setup

    //act

    //assertions

  }

}
