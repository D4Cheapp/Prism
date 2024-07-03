package com.prism.messenger.exception.password;

public class TooShortPasswordException extends Exception {
    public TooShortPasswordException() {
        super("Password is too short");
    }
}
