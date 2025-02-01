package com.healthly.symptomchecker.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(CustomExceptionHandler.class);

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ApiError> handleException(InvalidCredentialsException e,
                                                    HttpServletRequest request) {
        final HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;
        ApiError apiError = new ApiError(
                request.getRequestURI(),
                e.getMessage(),
                httpStatus.value(),
                LocalDateTime.now()
        );

        LOG.error(e.getMessage(), e);

        return new ResponseEntity<>(apiError, httpStatus);
    }

    @ExceptionHandler(ExistingUsernameException.class)
    public ResponseEntity<ApiError> handleException(ExistingUsernameException e,
                                                    HttpServletRequest request) {
        final HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        ApiError apiError = new ApiError(
                request.getRequestURI(),
                e.getMessage(),
                httpStatus.value(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(apiError, httpStatus);
    }

    @ExceptionHandler(AssessmentNotFoundException.class)
    public ResponseEntity<ApiError> handleException(AssessmentNotFoundException e,
                                                    HttpServletRequest request) {
        final HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        ApiError apiError = new ApiError(
                request.getRequestURI(),
                e.getMessage(),
                httpStatus.value(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(apiError, httpStatus);
    }

    @ExceptionHandler(InvalidSymptomException.class)
    public ResponseEntity<ApiError> handleException(InvalidSymptomException e,
                                                    HttpServletRequest request) {
        final HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        ApiError apiError = new ApiError(
                request.getRequestURI(),
                e.getMessage(),
                httpStatus.value(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(apiError, httpStatus);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleException(IllegalArgumentException e,
                                                    HttpServletRequest request) {
        final HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        ApiError apiError = new ApiError(
                request.getRequestURI(),
                e.getMessage(),
                httpStatus.value(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(apiError, httpStatus);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiError> handleException(IllegalStateException e,
                                                    HttpServletRequest request) {
        final HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        ApiError apiError = new ApiError(
                request.getRequestURI(),
                e.getMessage(),
                httpStatus.value(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(apiError, httpStatus);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleException(Exception e,
                                                    HttpServletRequest request) {
        final HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        ApiError apiError = new ApiError(
                request.getRequestURI(),
                e.getMessage(),
                httpStatus.value(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(apiError, httpStatus);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        final List<FieldError> fieldErrors = ex.getFieldErrors();

        final String errors = fieldErrors.stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining("; "));

        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(),
                errors, request.getDescription(false));

        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }
}
