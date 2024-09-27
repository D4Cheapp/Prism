package com.prism.messenger.exception.relation;

public class AddCurrentProfileToCurrentProfileException extends Exception {

  public AddCurrentProfileToCurrentProfileException() {
    super("You can't add yourself");
  }
}
