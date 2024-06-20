package com.messenger.prism.exception.auth;

public class TooManyAttemptsException extends Exception {

    public TooManyAttemptsException(String remainingStringTime) {
        super("Too many attempts. Try again later. Remaining time: " + remainingStringTime + " minutes");
    }
}
