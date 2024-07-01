package com.prism.authentication.exception.email;

public class EmptyEmailException extends Exception {
    public EmptyEmailException() {
        super("Email cannot be empty");
    }
}
