package com.messenger.prism.exception.auth;

public class IncorrectConfirmCodeException extends Exception {
    public IncorrectConfirmCodeException() {
        super("Incorrect confirm code");
    }

}
