package com.messenger.prism.exception.auth;

public class ActivationCodeExpireException extends Exception {
    public ActivationCodeExpireException() {
        super("Activation code not exist or expired");
    }
}
