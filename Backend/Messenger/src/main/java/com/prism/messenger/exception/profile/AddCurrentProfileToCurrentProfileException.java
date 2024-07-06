package com.prism.messenger.exception.profile;

public class AddCurrentProfileToCurrentProfileException extends Exception {

  public AddCurrentProfileToCurrentProfileException() {
    super("You can't add yourself");
  }
}
