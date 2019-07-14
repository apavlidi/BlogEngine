package com.blogEngine.profile;

import static com.blogEngine.blog.BlogControllerTest.asJsonString;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.blogEngine.controller.ProfileController;
import com.blogEngine.domain.Profile;
import com.blogEngine.restExceptions.ProfileNotFoundException;
import com.blogEngine.service.ProfileService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@RunWith(SpringRunner.class)
@WebMvcTest(ProfileController.class) //it only boots the component that is defined.
public class ProfileControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private ProfileService profileService;

  private static final String BASE_PROFILE_URL = "/profile";

  @Test
  public void getProfile_ShouldReturnProfile() throws Exception {
    given(profileService.getProfileByUsername(anyString())).willReturn(new Profile("alexis"));

    mockMvc.perform(MockMvcRequestBuilders.get(BASE_PROFILE_URL + "/alexis"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("username").value("alexis"));
  }

  @Test
  public void postProfile_ShouldReturnStatusOK() throws Exception {
    mockMvc.perform(
        MockMvcRequestBuilders.post(BASE_PROFILE_URL).content(asJsonString(new Profile("alexis")))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }

  @Test
  public void postInvalidProfile_ShouldReturnBadRequest() throws Exception {
    mockMvc.perform(
        MockMvcRequestBuilders.post(BASE_PROFILE_URL).content(asJsonString(new Profile(".")))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
  }

  @Test
  public void getNonExistenProfile_ShouldReturnNotFound() throws Exception {
    given(profileService.getProfileByUsername(anyString()))
        .willThrow(new ProfileNotFoundException());

    mockMvc.perform(MockMvcRequestBuilders.get(BASE_PROFILE_URL + "/nonexistant"))
        .andExpect(status().isNotFound());
  }

  @Test
  public void deleteProfile_ShouldReturnStatusOK() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.delete(BASE_PROFILE_URL + "/alexis"))
        .andExpect(status().isOk());
  }

  @Test
  public void updateProfile_ShouldReturnProfile() throws Exception {
    Profile newProfile = new Profile("babis");

    //given(profileService.updateProfile("alexis", newProfile))
    //   .willReturn(new Profile(""));

    ResultActions resultActions = mockMvc
        .perform(MockMvcRequestBuilders.put(BASE_PROFILE_URL + "/alexis")
            .content(asJsonString(newProfile))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("username").value("babis"))
        .andExpect(status().isOk());
    MvcResult result = resultActions.andReturn();
    String contentAsString = result.getResponse().getContentAsString();

    System.out.println();

  }

}
