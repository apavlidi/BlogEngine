package com.blogEngine.repository;

import com.blogEngine.domain.Profile;
import java.util.List;

public interface ProfileRepositoryCustom {

  Profile findByUsername(String username);

  Profile saveProfile(Profile profile);

  Profile deleteProfile(String username);

  Profile updateProfile(String username, Profile profileToBeUpdated);

  List<Profile> getAllProfiles();
}
