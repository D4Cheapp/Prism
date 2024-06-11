package com.messenger.prism.exception.auth;

public class TooShortLoginException extends Exception {
    public TooShortLoginException() {
        super("Login is too short");
    }
}
