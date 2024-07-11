package com.prism.messenger.exception;

public class PermissionsException extends Exception {

  public PermissionsException(String message) {
    super("Permissions error: " + message);
  }
}
