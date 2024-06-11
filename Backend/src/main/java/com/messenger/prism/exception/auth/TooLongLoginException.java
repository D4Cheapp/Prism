package com.messenger.prism.exception.auth;

public class TooLongLoginException extends Exception {
    public TooLongLoginException() {
        super("Login is too long");
    }
}
