package com.prism.messenger.exception.rabbitMQ;

public class IncorrectMessageActionException extends Exception {

  public IncorrectMessageActionException() {
    super("Incorrect message action");
  }
}
