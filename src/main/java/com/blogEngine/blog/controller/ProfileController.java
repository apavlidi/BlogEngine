package com.blogEngine.blog.controller;

import com.blogEngine.blog.domain.Profile;
import com.blogEngine.blog.service.ProfileService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "profile")
public class ProfileController {

  private final ProfileService profileService;

  public ProfileController(ProfileService profileService) {
    this.profileService = profileService;
  }

  @GetMapping("/{username}")
  private Profile getProfile(@PathVariable String username) {
    return profileService.getProfileByUsername(username);
  }

}
