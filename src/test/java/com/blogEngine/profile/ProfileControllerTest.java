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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@RunWith(SpringRunner.class)
@WebMvcTest(ProfileController.class) //it only boots the component that is defined.
public class ProfileControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private ProfileService profileService;

  @Test
  public void getProfile_ShouldReturnProfile() throws Exception {
    given(profileService.getProfileByUsername(anyString())).willReturn(new Profile("alexis"));

    mockMvc.perform(MockMvcRequestBuilders.get("/profile/alexis"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("username").value("alexis"));
  }

  @Test
  public void postProfile_ShouldReturnStatusOK() throws Exception {
    mockMvc.perform(
        MockMvcRequestBuilders.post("/profile").content(asJsonString(new Profile("alexis")))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }

  @Test
  public void getNonExistenProfile_ShouldReturnNotFound() throws Exception {
    given(profileService.getProfileByUsername(anyString()))
        .willThrow(new ProfileNotFoundException());

    mockMvc.perform(MockMvcRequestBuilders.get("/profile/nonexistant"))
        .andExpect(status().isNotFound());
  }

}
