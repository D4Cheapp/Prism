package com.prism.messenger.exception.profile;

public class ProfileNameIsTooLongException extends Exception {

  public ProfileNameIsTooLongException() {
    super("Profile name is too long");
  }
}
