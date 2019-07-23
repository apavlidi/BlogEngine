package com.blogEngine.profile;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import com.blogEngine.domain.Profile;
import com.blogEngine.repository.ProfileRepository;
import com.blogEngine.restExceptions.ProfileNotFoundException;
import com.blogEngine.service.ProfileService;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.stubbing.Answer;
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

  @Test
  public void deleteProfile_shouldReturnDeletedProfile() {
    Profile mockedProfile = new Profile("alexis");
    given(profileRepository.deleteProfile("alexis")).willReturn(mockedProfile);

    Profile deletedProfile = profileService.deleteProfile("alexis");

    assertThat(deletedProfile.getUsername()).isEqualTo("alexis");
  }

  @Test
  public void updateProfile_shouldReturnUpdatedProfile() {
    Profile profileToBeUpdated = new Profile("updated");

    Profile mockedUpdatedProfile = new Profile("updated");
    given(profileRepository.updateProfile("alexis", profileToBeUpdated))
        .willReturn(mockedUpdatedProfile);

    Profile updateProfile = profileService.updateProfile("alexis", profileToBeUpdated);

    assertThat(updateProfile.getUsername()).isNotNull();
    assertThat(updateProfile.getUsername()).isEqualTo(mockedUpdatedProfile.getUsername());
  }

  @Test(expected = ProfileNotFoundException.class)
  public void updateNotExistentProfile_shouldReturnProfileNotFoundException() {
    Profile profileToBeUpdated = new Profile("updated");

    given(profileRepository.updateProfile("alexis", profileToBeUpdated)).willReturn(null);

    profileService.updateProfile("alexis", profileToBeUpdated);
  }

  @Test
  public void getAllProfiles_shouldReturnProfiles() {
    List<Profile> profilesList = Arrays.asList(new Profile("profile1"), new Profile("profile2"));
    given(profileRepository.getAllProfiles())
        .willAnswer((Answer<List<Profile>>) invocation -> profilesList);

    List<Profile> allProfiles = profileService.getAllProfiles();

    assertThat(allProfiles).isNotNull();
    assertThat(allProfiles.size()).isEqualTo(2);
    assertThat(allProfiles.stream().anyMatch(findUsername("profile1"))).isTrue();
    assertThat(allProfiles.stream().anyMatch(findUsername("profile2"))).isTrue();
  }

  private Predicate<Profile> findUsername(String profile1) {
    return profile -> profile.getUsername().equals(profile1);
  }

}
