package com.blogEngine.blog.service;

import com.blogEngine.blog.domain.Profile;
import com.blogEngine.blog.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {

  @Autowired
  private ProfileRepository profileRepository;

  public Profile getProfileByUsername(String username) {
    return profileRepository.findByUsername(username);
  }

  public Profile saveProfile(Profile profile) {
    return profileRepository.save(profile);
  }

}
