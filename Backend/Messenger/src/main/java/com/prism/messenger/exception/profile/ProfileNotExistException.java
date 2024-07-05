package com.prism.messenger.exception.profile;

public class ProfileNotExistException extends Exception {

  public ProfileNotExistException() {
    super("Profile does not exist");
  }
}
