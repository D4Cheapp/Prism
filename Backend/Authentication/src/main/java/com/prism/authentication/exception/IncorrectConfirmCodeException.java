package com.prism.authentication.exception;

public class IncorrectConfirmCodeException extends Exception {
    public IncorrectConfirmCodeException() {
        super("Incorrect confirm code");
    }

}
