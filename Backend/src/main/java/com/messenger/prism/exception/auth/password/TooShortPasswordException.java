package com.messenger.prism.exception.auth.password;

public class TooShortPasswordException extends Exception{
    public TooShortPasswordException() {
        super("Password is too short");
    }
}
