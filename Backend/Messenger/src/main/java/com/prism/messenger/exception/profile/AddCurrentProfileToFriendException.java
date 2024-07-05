package com.prism.messenger.exception.profile;

public class AddCurrentProfileToFriendException extends Exception {

  public AddCurrentProfileToFriendException() {
    super("You can't add yourself to friends");
  }
}
