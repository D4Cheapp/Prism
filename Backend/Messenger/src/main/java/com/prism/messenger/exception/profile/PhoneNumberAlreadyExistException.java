package com.prism.messenger.exception.profile;

public class PhoneNumberAlreadyExistException extends Exception {

  public PhoneNumberAlreadyExistException() {
    super("Phone number already exist");
  }
}
