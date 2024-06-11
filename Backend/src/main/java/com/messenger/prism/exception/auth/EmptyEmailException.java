package com.messenger.prism.exception.auth;

public class EmptyEmailException extends Exception {
    public EmptyEmailException() {
        super("Email cannot be empty");
    }
}
