package com.messenger.prism.exception.auth.email;

public class EmptyEmailException extends Exception {
    public EmptyEmailException() {
        super("Email cannot be empty");
    }
}
