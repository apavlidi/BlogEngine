package com.blogEngine.blog;

import static com.blogEngine.blog.config.DatabaseProfiles.TEST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.OK;

import com.blogEngine.blog.domain.Profile;
import com.blogEngine.blog.repository.ProfileRepository;
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

}
