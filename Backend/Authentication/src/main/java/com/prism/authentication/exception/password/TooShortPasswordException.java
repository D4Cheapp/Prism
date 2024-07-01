package com.prism.authentication.exception.password;

public class TooShortPasswordException extends Exception {
    public TooShortPasswordException() {
        super("Password is too short");
    }
}
