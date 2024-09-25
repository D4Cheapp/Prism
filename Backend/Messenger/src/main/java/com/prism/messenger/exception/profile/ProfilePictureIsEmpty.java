package com.prism.messenger.exception.profile;

public class ProfilePictureIsEmpty extends RuntimeException {

  public ProfilePictureIsEmpty() {
    super("Profile picture is empty");
  }
}
