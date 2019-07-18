package com.blogEngine.profile;

import static com.blogEngine.config.DatabaseProfiles.TEST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

import java.util.ArrayList;
import java.util.List;
import com.blogEngine.MongoCleanupRule;
import com.blogEngine.domain.Blog;
import com.blogEngine.domain.Profile;
import com.blogEngine.repository.ProfileRepository;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT) //it boots a server
@ActiveProfiles(TEST)
public class ProfileIT {

  @Rule
  public final MongoCleanupRule cleanupRule = new MongoCleanupRule(this, Profile.class);

  @Autowired
  private ProfileRepository profileRepository;

  @Autowired
  private TestRestTemplate restTemplate;

  @Autowired
  private MongoTemplate mongoTemplate;

  @Test
  public void getProfile_shouldReturnProfileDetails() {
    Profile profile = new Profile();
    profile.setUsername("alexis");
    profileRepository.save(profile);

    ResponseEntity<Profile> response = restTemplate.getForEntity("/profile/alexis", Profile.class);

    assertThat(response.getStatusCode()).isEqualTo(OK);
    assertThat(response.getBody().getUsername()).isEqualTo("alexis");
  }

  @Test
  public void deleteProfile_shouldReturnStatusOk() {
    Profile profile = new Profile();
    profile.setUsername("alexis");
    profileRepository.save(profile);

    restTemplate.delete("/profile/alexis", Profile.class);

    Profile profileDeleted = profileRepository.findByUsername("alexis");

    assertThat(profileDeleted).isEqualTo(null);
  }

  @Test
  public void updateProfile_shouldReturnUpdatedProfileDetails() {
    Profile profile = new Profile();
    profile.setUsername("alexis");
    profileRepository.save(profile);

    restTemplate.put("/profile/alexis", new Profile("test"));

    Profile oldProfile = profileRepository.findByUsername("alexis");
    Profile newProfile = profileRepository.findByUsername("test");

    assertThat(oldProfile).isEqualTo(null);
    assertThat(newProfile).isNotEqualTo(null);
    assertThat(newProfile.getUsername()).isNotEqualTo(null);
    assertThat(newProfile.getUsername()).isEqualTo("test");
  }

  @Test
  public void updateNonProfile_shouldReturnNotFound() {
    Profile profileToBeUpdated = new Profile("alexis");
    HttpEntity<Profile> profileHttpEntity = new HttpEntity<>(profileToBeUpdated);

    ResponseEntity<Profile> exchange = restTemplate
        .exchange("/profile/alexis", PUT, profileHttpEntity, Profile.class);

    assertThat(exchange.getStatusCode()).isEqualTo(NOT_FOUND);
  }

  @Test
  public void postProfileWithBlogs_shouldReturnStatusOK() {
    //setup
    Profile tempProfile = new Profile("tempUsername");
    List<Blog> tempBlogs = new ArrayList<>();
    Blog tempBlog = new Blog("test title");

    tempBlog.setText("test text");
    tempBlog.setProfile(tempProfile);
    tempBlogs.add(tempBlog);
    tempProfile.setBlogs(tempBlogs);

    //act
    ResponseEntity<String> response = restTemplate.postForEntity("/profile", tempProfile, String.class);

    //assertions
    assertThat(response.getStatusCode()).isEqualTo(OK);
  }

  @Test
  public void postProfileWithInvalidBlogs_shouldReturnBadRequest() {
    //setup
    Profile tempProfile = new Profile("tempUsername");
    List<Blog> tempBlogs = new ArrayList<>();
    Blog tempBlog = new Blog();

    tempBlog.setProfile(tempProfile);
    tempBlogs.add(tempBlog);
    tempProfile.setBlogs(tempBlogs);

    //act
    ResponseEntity<String> response = restTemplate.postForEntity("/profile", tempProfile, String.class);

    //assertions
    assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);
  }

}
