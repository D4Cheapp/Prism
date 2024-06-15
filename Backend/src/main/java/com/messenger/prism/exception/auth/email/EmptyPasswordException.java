package com.messenger.prism.exception.auth.email;

public class EmptyPasswordException extends Exception {
    public EmptyPasswordException() {
        super("Password cannot be empty");
    }
}
