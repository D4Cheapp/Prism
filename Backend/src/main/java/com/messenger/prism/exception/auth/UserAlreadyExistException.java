package com.messenger.prism.exception.auth;

public class UserAlreadyExistException extends Exception {
    public UserAlreadyExistException() {
        super("User with this email already exists");
    }
}
