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
import java.util.Objects;
import javax.validation.constraints.Size;
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
  private final String BASE_BLOG_URL = "/blogs";

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

    ResponseEntity<Blog> response = restTemplate.getForEntity(BASE_BLOG_URL + "/title", Blog.class);

    assertThat(response.getStatusCode()).isEqualTo(OK);
    assertThat(Objects.requireNonNull(response.getBody()).getText()).isEqualTo("test");
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
        .postForEntity(BASE_BLOG_URL, request, String.class);

    assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);
  }

  @Test
  public void postBlogWithInvalidProfile_shouldReturnBadRequest() {
    Blog invalidBlog = new Blog("Some valid title");
    invalidBlog.setText("Some valid text");
    invalidBlog.setProfile(new Profile());

    ResponseEntity<String> response = restTemplate
        .postForEntity(BASE_BLOG_URL, invalidBlog, String.class);

    assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);
  }

  @Test
  public void postBlogWithProfile_shouldReturnOK() {
    Blog testBlog = new Blog("Some valid title");
    testBlog.setText("Some valid text");
    testBlog.setProfile(new Profile("testProfile"));

    ResponseEntity<String> response = restTemplate
        .postForEntity(BASE_BLOG_URL, testBlog, String.class);

    assertThat(response.getStatusCode()).isEqualTo(OK);
  }

  @Test
  public void postBlogWithNoTitle_shouldReturnBadRequest() {
    Blog blog = new Blog();

    ResponseEntity<Blog> response = restTemplate
        .postForEntity(BASE_BLOG_URL, blog, Blog.class);

    assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);
  }

  @Test
  public void postBlogWithLessThanMinTitle_shouldReturnBadRequest() throws NoSuchFieldException {
    Blog blog = new Blog();

    int minTitleLength = getSizePropertyForTitleAnnotation("title", "min");
    String title = getStringWithLength(minTitleLength - 1);
    blog.setTitle(title);
    blog.setText("test");

    ResponseEntity<Blog> response = restTemplate
        .postForEntity(BASE_BLOG_URL, blog, Blog.class);

    assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);
  }

  @Test
  public void postBlogWithMoreThanMaxTitle_shouldReturnBadRequest() throws NoSuchFieldException {
    Blog blog = new Blog();

    int maxTitleLength = getSizePropertyForTitleAnnotation("title", "max");
    String title = getStringWithLength(maxTitleLength + 1);
    blog.setTitle(title);
    blog.setText("test");

    ResponseEntity<Blog> response = restTemplate
        .postForEntity(BASE_BLOG_URL, blog, Blog.class);

    assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);
  }

  @Test
  public void postBlogWithLessThanMinText_shouldReturnBadRequest() throws NoSuchFieldException {
    Blog blog = new Blog();

    int minTextLength = getSizePropertyForTitleAnnotation("text", "min");
    String text = getStringWithLength(minTextLength - 1);
    blog.setTitle("thats a correct title");
    blog.setText(text);

    ResponseEntity<Blog> response = restTemplate
        .postForEntity(BASE_BLOG_URL, blog, Blog.class);

    assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);
  }

  @Test
  public void postBlogWithoutProfile_shouldReturnBadRequest() {
    Blog blog = new Blog();

    blog.setTitle("thats a correct title");
    blog.setText("thats another correct text");

    ResponseEntity<Blog> response = restTemplate
        .postForEntity(BASE_BLOG_URL, blog, Blog.class);

    assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);
  }


  @Test
  public void postBlogWithoutValidProfile_shouldReturnBadRequest() {
    Blog blog = new Blog();
    Profile profile = new Profile();

    blog.setProfile(profile);
    blog.setTitle("thats a correct title");
    blog.setText("thats another correct text");

    ResponseEntity<Blog> response = restTemplate
        .postForEntity(BASE_BLOG_URL, blog, Blog.class);

    assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);
  }

  private int getSizePropertyForTitleAnnotation(String field, String property)
      throws NoSuchFieldException {
    Class<?> blogClass = Blog.class;
    Size annotation = blogClass.getDeclaredField(field).getAnnotation(Size.class);
    return property.equals("max") ? annotation.max() : annotation.min();
  }

  private String getStringWithLength(int maxTitleLength) {
    StringBuilder title = new StringBuilder();
    for (int i = 0; i < maxTitleLength; i++) {
      title.append("0");
    }

    return String.valueOf(title);
  }

}
