package com.blogEngine.service;

import com.blogEngine.domain.Profile;
import com.blogEngine.repository.ProfileRepository;
import com.blogEngine.restExceptions.ProfileNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {

  @Autowired
  private ProfileRepository profileRepository;

  public Profile getProfileByUsername(String username) {
    Profile profile = profileRepository.findByUsername(username);
    if (profile == null) {
      throw new ProfileNotFoundException();
    }
    return profile;
  }

  public Profile saveProfile(Profile profile) {
    return profileRepository.save(profile);
  }

  public Profile deleteProfile(String username) {
    return profileRepository.deleteProfile(username);
  }

  public Profile updateProfile(String username, Profile newProfile) {
    Profile updatedProfile = profileRepository.updateProfile(username, newProfile);
    if (updatedProfile == null) {
      throw new ProfileNotFoundException();
    }
    return updatedProfile;
  }

}
