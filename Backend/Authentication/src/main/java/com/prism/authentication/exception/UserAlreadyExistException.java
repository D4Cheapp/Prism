package com.prism.authentication.exception;

public class UserAlreadyExistException extends Exception {
    public UserAlreadyExistException() {
        super("User with this email already exists");
    }
}
