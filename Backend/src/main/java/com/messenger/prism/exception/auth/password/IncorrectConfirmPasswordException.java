package com.messenger.prism.exception.auth.password;

public class IncorrectConfirmPasswordException extends Exception {
    public IncorrectConfirmPasswordException() {
        super("Incorrect confirm password");
    }
}
