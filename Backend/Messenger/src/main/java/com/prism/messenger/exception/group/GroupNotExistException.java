package com.prism.messenger.exception.group;

public class GroupNotExistException extends Exception {

  public GroupNotExistException() {
    super("Group does not exist");
  }
}
