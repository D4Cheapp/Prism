package com.prism.authentication.exception.password;

public class IncorrectPasswordException extends Exception {
    public IncorrectPasswordException() {
        super("Incorrect password");
    }
}
