package com.blogEngine.blog;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import com.blogEngine.blog.domain.Profile;
import com.blogEngine.blog.repository.ProfileRepository;
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

}
