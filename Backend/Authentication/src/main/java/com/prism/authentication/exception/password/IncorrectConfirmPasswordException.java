package com.prism.authentication.exception.password;

public class IncorrectConfirmPasswordException extends Exception {
    public IncorrectConfirmPasswordException() {
        super("Incorrect confirm password");
    }
}
