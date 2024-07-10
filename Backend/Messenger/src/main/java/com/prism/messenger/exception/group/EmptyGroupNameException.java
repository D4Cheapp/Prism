package com.prism.messenger.exception.group;

public class EmptyGroupNameException extends Exception {

  public EmptyGroupNameException() {
    super("Group name cannot be empty");
  }
}
