package com.prism.messenger.util;

import com.prism.messenger.entity.Profile;
import com.prism.messenger.exception.profile.ProfileNotExistException;
import com.prism.messenger.repository.ProfileRepository;
import java.util.Optional;

public class ProfileUtil {

  public static Profile getProfileByEmail(String email, ProfileRepository profileRepository)
      throws ProfileNotExistException {
    Optional<Profile> profile = profileRepository.findByEmail(email);
    boolean isProfileNotFound = profile.isEmpty();
    if (isProfileNotFound) {
      throw new ProfileNotExistException();
    }
    return profile.get();
  }

  public static Profile getProfileByTag(String tag, ProfileRepository profileRepository)
      throws ProfileNotExistException {
    Optional<Profile> profile = profileRepository.findByTag(tag);
    boolean isProfileNotFound = profile.isEmpty();
    if (isProfileNotFound) {
      throw new ProfileNotExistException();
    }
    return profile.get();
  }
}