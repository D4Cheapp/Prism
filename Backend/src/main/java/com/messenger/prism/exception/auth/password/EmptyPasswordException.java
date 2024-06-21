package com.messenger.prism.exception.auth.password;

public class EmptyPasswordException extends Exception {
    public EmptyPasswordException() {
        super("Password cannot be empty");
    }
}
