package com.messenger.prism.exception.auth;

public class PasswordIsTooWeakException extends Exception {
    public PasswordIsTooWeakException() {
        super("Password is too weak");
    }
}
