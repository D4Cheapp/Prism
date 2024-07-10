package com.prism.messenger.util;

import com.prism.messenger.entity.Profile;
import com.prism.messenger.exception.profile.ProfileNotExistException;
import java.util.Optional;

public class ProfileUtil {

  public static Profile getProfileBy(String arg, PheckIsProfileExistFunction method)
      throws ProfileNotExistException {
    Optional<Profile> profile = method.method(arg);
    boolean isProfileNotFound = profile.isEmpty();
    if (isProfileNotFound) {
      throw new ProfileNotExistException();
    }
    return profile.get();
  }

  @FunctionalInterface
  public interface PheckIsProfileExistFunction {

    Optional<Profile> method(String arg);
  }
}