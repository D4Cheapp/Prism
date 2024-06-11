package com.messenger.prism.exception.auth;

public class TooShortPasswordException extends Exception{
    public TooShortPasswordException() {
        super("Password is too short");
    }
}
