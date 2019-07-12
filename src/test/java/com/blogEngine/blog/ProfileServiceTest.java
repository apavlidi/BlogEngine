package com.blogEngine.blog;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import com.blogEngine.blog.domain.Profile;
import com.blogEngine.blog.repository.ProfileRepository;
import com.blogEngine.blog.service.ProfileService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@RunWith(SpringRunner.class)
public class ProfileServiceTest {

  @Autowired
  private ProfileService profileService;

  @MockBean
  private ProfileRepository profileRepository;

  @Test
  public void getProfileByUsername_shouldReturnProfileDetails() {
    given(profileRepository.findByUsername(anyString())).willReturn(new Profile("alexis"));

    Profile profile = profileService.getProfileByUsername("alexis");

    assertThat(profile.getUsername()).isEqualTo("alexis");
  }

  @Test
  public void postProfile_shouldReturnProfile() {
    Profile profile = new Profile("alexis");

    Profile mockedProfile = new Profile("alexis");
    given(profileRepository.save(profile)).willReturn(mockedProfile);

    Profile savedProfile = profileService.saveProfile(profile);

    assertThat(savedProfile.getUsername()).isEqualTo(mockedProfile.getUsername());
  }

}
