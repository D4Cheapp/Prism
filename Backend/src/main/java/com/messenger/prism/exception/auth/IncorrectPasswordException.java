package com.messenger.prism.exception.auth;

public class IncorrectPasswordException extends Exception {
    public IncorrectPasswordException() {
        super("Incorrect password");
    }
}
