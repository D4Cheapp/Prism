package com.prism.authentication.exception.password;

public class TooLongPasswordException extends Exception {
    public TooLongPasswordException() {
        super("Password is too long");
    }
}
