package com.prism.messenger.exception.profile;

public class StatusIsTooLongException extends Exception {

  public StatusIsTooLongException() {
    super("Status is too long");
  }
}
