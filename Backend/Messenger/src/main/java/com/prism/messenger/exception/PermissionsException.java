package com.prism.messenger.exception;

public class PermissionsException extends Exception {

  public PermissionsException() {
    super("You don't have permissions");
  }
}
