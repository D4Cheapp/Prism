package com.messenger.prism.exception.auth.password;

public class PasswordIsTooWeakException extends Exception {
    public PasswordIsTooWeakException(String mustInclude) {
        super("Password is too weak: password must include at least one " + mustInclude);
    }
}
