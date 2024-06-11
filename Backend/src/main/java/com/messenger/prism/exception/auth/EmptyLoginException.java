package com.messenger.prism.exception.auth;

public class EmptyLoginException extends Exception {
    public EmptyLoginException() {
        super("Login cannot be empty");
    }
}
