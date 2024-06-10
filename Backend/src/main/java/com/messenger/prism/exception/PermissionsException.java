package com.messenger.prism.exception;

public class PermissionsException extends Exception {
    public PermissionsException() {
        super("You don't have permissions");
    }
}
