package com.prism.messenger.exception.email;

public class EmptyEmailException extends Exception {
    public EmptyEmailException() {
        super("Email cannot be empty");
    }
}
