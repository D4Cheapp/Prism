package com.messenger.prism.exception.auth.password;

public class PasswordIsTooWeakException extends Exception {
    public PasswordIsTooWeakException(String message) {
        super("Password is too weak: " + message);
    }
}
