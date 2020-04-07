package com.blogEngine.controller;

import static org.springframework.http.HttpStatus.NOT_FOUND;

import com.blogEngine.domain.Profile;
import com.blogEngine.restExceptions.ProfileNotFoundException;
import com.blogEngine.service.ProfileService;
import java.util.List;
import javax.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

  @GetMapping
  private List<Profile> getAllProfiles() {
    return profileService.getAllProfiles();
  }

  @GetMapping("/{username}")
  private Profile getProfile(@PathVariable String username) {
    return profileService.getProfileByUsername(username);
  }

  @PostMapping
  private Profile postProfile(@Valid @RequestBody Profile profile) {
    return profileService.saveProfile(profile);
  }

  @DeleteMapping("/{username}")
  private Profile deleteProfile(@PathVariable String username) {
    return profileService.deleteProfile(username);
  }

  @PutMapping(value = "/{username}", produces = {MediaType.APPLICATION_JSON_VALUE})
  private Profile updateProfile(@RequestBody Profile profile, @PathVariable String username) {
    return profileService.updateProfile(username, profile);
  }

  @ExceptionHandler
  @ResponseStatus(NOT_FOUND)
  private void profileNotFoundException(ProfileNotFoundException ex) {
  }

}
