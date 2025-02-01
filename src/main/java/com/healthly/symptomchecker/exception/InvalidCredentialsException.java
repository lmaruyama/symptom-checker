package com.healthly.symptomchecker.exception;

public class InvalidCredentialsException extends RuntimeException{

    public InvalidCredentialsException() {
        super("User or password invalid");
    }
}
