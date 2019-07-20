package com.blogEngine.profile;

import static com.blogEngine.config.DatabaseProfiles.TEST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import com.blogEngine.MongoCleanupRule;
import com.blogEngine.domain.Profile;
import com.blogEngine.repository.ProfileRepository;
import java.util.Objects;
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

  private String DOMAIN_PROFILE_URL = "/profile";

  @Test
  public void getProfile_shouldReturnProfileDetails() {
    Profile profile = new Profile();
    profile.setUsername("alexis");
    profileRepository.save(profile);

    ResponseEntity<Profile> response = restTemplate.getForEntity(DOMAIN_PROFILE_URL + "/alexis", Profile.class);

    assertThat(response.getStatusCode()).isEqualTo(OK);
    assertThat(Objects.requireNonNull(response.getBody()).getUsername()).isEqualTo("alexis");
  }

  @Test
  public void deleteProfile_shouldReturnStatusOk() {
    Profile profile = new Profile();
    profile.setUsername("alexis");
    profileRepository.save(profile);

    restTemplate.delete(DOMAIN_PROFILE_URL + "/alexis", Profile.class);

    Profile profileDeleted = profileRepository.findByUsername("alexis");

    assertThat(profileDeleted).isEqualTo(null);
  }

  @Test
  public void updateProfile_shouldReturnUpdatedProfileDetails() {
    Profile profile = new Profile();
    profile.setUsername("alexis");
    profileRepository.save(profile);

    restTemplate.put(DOMAIN_PROFILE_URL + "/alexis", new Profile("test"));

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
        .exchange(DOMAIN_PROFILE_URL + "/alexis", PUT, profileHttpEntity, Profile.class);

    assertThat(exchange.getStatusCode()).isEqualTo(NOT_FOUND);
  }

  @Test
  public void postProfileWithNoneExistentParameters_shouldReturnBadRequest() throws JSONException {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(APPLICATION_JSON);
    JSONObject profileJsonObject = new JSONObject();
    profileJsonObject.put("fake_field1", "fake none existent field");
    profileJsonObject.put("username", "testUser");

    HttpEntity<String> request = new HttpEntity<>(profileJsonObject.toString(), headers);
    ResponseEntity<String> response = restTemplate
        .postForEntity(DOMAIN_PROFILE_URL, request, String.class);

    assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);
  }

}
