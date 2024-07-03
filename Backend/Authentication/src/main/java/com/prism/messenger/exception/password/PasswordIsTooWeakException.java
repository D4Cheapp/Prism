package com.prism.messenger.exception.password;

public class PasswordIsTooWeakException extends Exception {
    public PasswordIsTooWeakException(String mustInclude) {
        super("Password is too weak: password must include at least one " + mustInclude);
    }
}
