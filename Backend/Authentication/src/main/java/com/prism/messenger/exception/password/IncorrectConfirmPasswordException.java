package com.prism.messenger.exception.password;

public class IncorrectConfirmPasswordException extends Exception {

  public IncorrectConfirmPasswordException() {
    super("Incorrect confirm password");
  }
}
