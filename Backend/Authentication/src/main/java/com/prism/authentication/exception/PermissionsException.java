package com.prism.authentication.exception;

public class PermissionsException extends Exception {
    public PermissionsException() {
        super("You don't have permissions");
    }
}
