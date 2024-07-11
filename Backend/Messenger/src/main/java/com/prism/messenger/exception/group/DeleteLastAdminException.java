package com.prism.messenger.exception.group;

public class DeleteLastAdminException extends Exception {

  public DeleteLastAdminException() {
    super("You can't delete last admin of group");
  }
}
