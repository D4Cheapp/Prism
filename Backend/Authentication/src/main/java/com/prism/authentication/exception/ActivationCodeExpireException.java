package com.prism.authentication.exception;

public class ActivationCodeExpireException extends Exception {
    public ActivationCodeExpireException() {
        super("Activation code not exist or expired");
    }
}
