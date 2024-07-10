package com.prism.messenger.exception.chat;

public class ChatAlreadyExistException extends Exception {

  public ChatAlreadyExistException() {
    super("Chat already exist");
  }
}
