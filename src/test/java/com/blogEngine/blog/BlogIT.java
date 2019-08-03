package com.blogEngine.blog;

import com.blogEngine.MongoCleanupRule;
import com.blogEngine.config.DatabaseProfiles;
import com.blogEngine.domain.Blog;
import com.blogEngine.domain.Profile;
import com.blogEngine.repository.BlogRepository;
import com.blogEngine.repository.ProfileRepository;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Objects;

import static com.blogEngine.Utilies.getSizePropertyForFieldAnnotation;
import static com.blogEngine.Utilies.getStringWithLength;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;

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
  private ProfileRepository profileRepository;

  @Autowired
  private MongoTemplate mongoTemplate;

  @Test
  public void getBlog_returnBlogDetails() {
    Blog blog = new Blog("title");
    blog.setText("test");
    blogRepository.saveBlog(blog);

    ResponseEntity<Blog> response = restTemplate.getForEntity(BASE_BLOG_URL + "/title", Blog.class);

    assertThat(response.getStatusCode()).isEqualTo(OK);
    assertThat(Objects.requireNonNull(response.getBody()).getText()).isEqualTo("test");
    assertThat(response.getBody().getDate()).isNotNull();
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
    Profile testProfile = new Profile("testProfile");
    profileRepository.save(testProfile);

    Blog testBlog = new Blog("Some valid title");
    testBlog.setText("Some valid text");
    testBlog.setProfile(testProfile);

    ResponseEntity<Blog> response = restTemplate
        .postForEntity(BASE_BLOG_URL, testBlog, Blog.class);

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

    int minTitleLength = getSizePropertyForFieldAnnotation("title", "min", Blog.class);
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

    int maxTitleLength = getSizePropertyForFieldAnnotation("title", "max", Blog.class);
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

    int minTextLength = getSizePropertyForFieldAnnotation("text", "min", Blog.class);
    String text = getStringWithLength(minTextLength - 1);
    blog.setTitle("thats a correct title");
    blog.setText(text);
    blog.setProfile(new Profile("testUser"));

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

  @Test
  public void getBlogs_shouldReturnAllBlogs() {
    Profile profile = new Profile("alexis");
    profileRepository.save(profile);

    Blog blog = new Blog("title");
    blog.setTitle("test title");
    blog.setText("test");
    blog.setProfile(profile);
    blogRepository.saveBlog(blog);

    profile = new Profile("george");
    profileRepository.save(profile);

    blog = new Blog("title2");
    blog.setTitle("test title2");
    blog.setText("test2");
    blog.setProfile(profile);
    blogRepository.saveBlog(blog);

    ResponseEntity<List<Blog>> response =
        restTemplate.exchange(BASE_BLOG_URL, GET,
            null, new ParameterizedTypeReference<List<Blog>>() {
            });
    List<Blog> blogs = response.getBody();

    assertThat(blogs).isNotNull();
    assertThat(blogs.size()).isEqualTo(2);
    assertThat(blogs.get(0).getTitle()).isEqualTo("test title");
    assertThat(blogs.get(1).getTitle()).isEqualTo("test title2");
  }

  @Test
  @Ignore
  public void getBlogsBasedOnProfileId_shouldReturnAllBlogsOfProfile() {
    Profile profile = new Profile("alexis");
    profileRepository.save(profile);

    Blog blog = new Blog("title");
    blog.setTitle("test title");
    blog.setText("test");
    blog.setProfile(profile);
    blogRepository.saveBlog(blog);

    Blog blogRetrievedFromDb = blogRepository.findByTitle("test title");
    String profileId = blogRetrievedFromDb.getProfile().getId();

    ResponseEntity<Blog> blogsBasedOnProfile = restTemplate
        .getForEntity(BASE_BLOG_URL + "?q={\"profileId\":" + profileId, Blog.class);
  }



}
