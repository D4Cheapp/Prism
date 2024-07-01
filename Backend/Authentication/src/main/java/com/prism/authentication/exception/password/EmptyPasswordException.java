package com.prism.authentication.exception.password;

public class EmptyPasswordException extends Exception {
    public EmptyPasswordException() {
        super("Password cannot be empty");
    }
}
