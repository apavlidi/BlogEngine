package com.blogEngine.controller;

import static org.springframework.http.HttpStatus.NOT_FOUND;

import com.blogEngine.domain.Profile;
import com.blogEngine.restExceptions.ProfileNotFoundException;
import com.blogEngine.service.ProfileService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
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

  @PostMapping
  private String postProfile(@RequestBody Profile profile) {
    profileService.saveProfile(profile);
    return HttpStatus.OK.toString();
  }

  @ExceptionHandler
  @ResponseStatus(NOT_FOUND)
  private void profileNotFoundException(ProfileNotFoundException ex) {

  }

}
