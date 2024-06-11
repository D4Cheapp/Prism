package com.messenger.prism.exception.auth;

public class EmptyPasswordException extends Exception {
    public EmptyPasswordException() {
        super("Password cannot be empty");
    }
}
