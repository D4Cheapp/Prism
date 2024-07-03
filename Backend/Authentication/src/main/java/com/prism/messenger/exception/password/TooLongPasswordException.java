package com.prism.messenger.exception.password;

public class TooLongPasswordException extends Exception {
    public TooLongPasswordException() {
        super("Password is too long");
    }
}
