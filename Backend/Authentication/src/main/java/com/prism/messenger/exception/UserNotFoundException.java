package com.prism.messenger.exception;

public class UserNotFoundException extends Exception {

  public UserNotFoundException() {
    super("User not found");
  }
}
