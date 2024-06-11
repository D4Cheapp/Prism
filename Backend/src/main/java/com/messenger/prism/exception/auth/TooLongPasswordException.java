package com.messenger.prism.exception.auth;

public class TooLongPasswordException extends Exception {
    public TooLongPasswordException() {
        super("Password is too long");
    }
}
