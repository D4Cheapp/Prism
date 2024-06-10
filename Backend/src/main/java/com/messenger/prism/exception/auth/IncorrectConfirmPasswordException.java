package com.messenger.prism.exception.auth;

public class IncorrectConfirmPasswordException extends Exception {
    public IncorrectConfirmPasswordException() {
        super("Incorrect confirm password");
    }
}
