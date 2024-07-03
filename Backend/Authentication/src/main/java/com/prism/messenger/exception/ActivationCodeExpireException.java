package com.prism.messenger.exception;

public class ActivationCodeExpireException extends Exception {
    public ActivationCodeExpireException() {
        super("Activation code not exist or expired");
    }
}
