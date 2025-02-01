package com.healthly.symptomchecker.exception;

public class AssessmentNotFoundException extends RuntimeException {

    public AssessmentNotFoundException() {
        super("Assessment not found");
    }
}
