package com.blogEngine.profile;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import com.blogEngine.MongoCleanupRule;
import com.blogEngine.domain.Profile;
import com.blogEngine.repository.ProfileRepository;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class ProfileRepositoryTest {

  @Rule
  public final MongoCleanupRule cleanupRule = new MongoCleanupRule(this, Profile.class);

  @Autowired
  private MongoTemplate mongoTemplate;

  @Autowired
  private ProfileRepository profileRepository;

  @Test
  public void findByUsername_ShouldReturnProfile() {
    profileRepository.save(new Profile("alexis"));
    Profile profile = profileRepository.findByUsername("alexis");
    assertThat(profile.getUsername()).isEqualTo("alexis");
  }

  @Test
  public void saveProfile_ShouldReturnProfile() {
    String username = "random name";

    Profile profile = profileRepository.saveProfile(new Profile(username));

    assertThat(profile.getUsername()).isEqualTo(username);
  }

  @Test
  public void deleteProfile_ShouldReturnDeletedProfile() {
    profileRepository.save(new Profile("alexis"));
    Profile deletedProfile = profileRepository.deleteProfile("alexis");

    assertThat(deletedProfile.getUsername()).isEqualTo("alexis");
  }

  @Test
  public void updateProfile_shouldReturnUpdatedProfile() {
    profileRepository.save(new Profile("alexis"));

    Profile profileToBeUpdated = new Profile("updated username");
    Profile updatedProfile = profileRepository.updateProfile("alexis", profileToBeUpdated);

    assertThat(updatedProfile.getUsername()).isEqualTo(profileToBeUpdated.getUsername());
  }

  @Test
  public void updateOfNotExistentProfile_shouldReturnNull() {
    Profile profileToBeUpdated = new Profile("updated username");
    Profile updatedProfile = profileRepository.updateProfile("alexis", profileToBeUpdated);

    assertThat(updatedProfile).isEqualTo(null);
  }


}
