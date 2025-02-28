package com.prism.messenger.exception;

public class UserAlreadyExistException extends Exception {

  public UserAlreadyExistException() {
    super("User with this email already exists");
  }
}
