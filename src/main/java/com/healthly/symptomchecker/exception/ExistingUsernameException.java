package com.healthly.symptomchecker.exception;

public class ExistingUsernameException extends RuntimeException {

    public ExistingUsernameException() {
        super("This email is already registered. Please provide another one");
    }
}
