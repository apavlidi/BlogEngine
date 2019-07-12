package com.blogEngine.blog.repository;

import com.blogEngine.blog.domain.Profile;

public interface ProfileRepositoryCustom {

  Profile findByUsername(String username);

  Profile saveProfile(Profile profile);

  Profile deleteProfile(String username);

}
