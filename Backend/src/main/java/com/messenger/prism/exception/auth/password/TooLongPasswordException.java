package com.messenger.prism.exception.auth.password;

public class TooLongPasswordException extends Exception {
    public TooLongPasswordException() {
        super("Password is too long");
    }
}
