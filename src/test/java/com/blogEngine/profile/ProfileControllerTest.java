package com.blogEngine.profile;

import static com.blogEngine.blog.BlogControllerTest.asJsonString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.blogEngine.BaseController;
import com.blogEngine.controller.ProfileController;
import com.blogEngine.domain.Profile;
import com.blogEngine.restExceptions.ProfileNotFoundException;
import com.blogEngine.service.ProfileService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ProfileController.class) //it only boots the component that is defined.
public class ProfileControllerTest extends BaseController {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private ProfileService profileService;

  private static final String BASE_PROFILE_URL = "/profile";

  @Test
  public void getProfile_ShouldReturnProfile() throws Exception {
    given(profileService.getProfileByUsername(anyString())).willReturn(new Profile("alexis"));

    mockMvc.perform(get(BASE_PROFILE_URL + "/alexis"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("username").value("alexis"));
  }

  @Test
  public void postProfile_ShouldReturnStatusOK() throws Exception {

    Profile mockProfile = new Profile("testProfile");

    given(profileService.saveProfile(any(Profile.class))).willReturn(mockProfile);

    mockMvc.perform(post(BASE_PROFILE_URL)
        .contentType(APPLICATION_JSON)
        .content(asJsonString(mockProfile))
        .accept(APPLICATION_JSON))
        .andExpect(jsonPath("username").value(mockProfile.getUsername()))
        .andExpect(status().isOk());

  }

  @Test
  public void postInvalidProfile_ShouldReturnBadRequest() throws Exception {
    mockMvc.perform(
        post(BASE_PROFILE_URL).content(asJsonString(new Profile(".")))
            .contentType(APPLICATION_JSON))
        .andExpect(status().isBadRequest());
  }

  @Test
  public void getNonExistenProfile_ShouldReturnNotFound() throws Exception {
    given(profileService.getProfileByUsername(anyString()))
        .willThrow(new ProfileNotFoundException());

    mockMvc.perform(get(BASE_PROFILE_URL + "/nonexistant"))
        .andExpect(status().isNotFound());
  }

  @Test
  public void deleteProfile_ShouldReturnDeletedProfile() throws Exception {
    String testUsername = "testUsername";

    given(profileService.deleteProfile(anyString())).willReturn(new Profile(testUsername));

    mockMvc.perform(delete(BASE_PROFILE_URL + "/" + testUsername)
        .accept(APPLICATION_JSON))
        .andExpect(jsonPath("username").value(testUsername))
        .andExpect(status().isOk());
  }

  @Test
  public void updateProfile_ShouldReturnProfile() throws Exception {
    Profile newProfile = new Profile("babis");

    given(profileService.updateProfile(anyString(), any(Profile.class)))
        .willReturn(new Profile("test"));

    mockMvc.perform(put(BASE_PROFILE_URL + "/alexis")
        .content(asJsonString(newProfile))
        .contentType(APPLICATION_JSON))
        .andExpect(jsonPath("username").value("test"))
        .andExpect(status().isOk());
  }

  @Test
  public void updateNonProfile_shouldReturnNotFound() throws Exception {
    Profile newProfile = new Profile("babis");

    given(profileService.updateProfile(anyString(), any(Profile.class)))
        .willThrow(new ProfileNotFoundException());

    mockMvc.perform(
        put(BASE_PROFILE_URL + "/alexis").content(asJsonString(newProfile))
            .contentType(APPLICATION_JSON)).andExpect(status().isNotFound());
  }

}
