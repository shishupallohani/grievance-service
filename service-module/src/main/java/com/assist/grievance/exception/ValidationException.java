package com.assist.grievance.exception;

import org.springframework.http.HttpStatus;

public class ValidationException extends BusinessException{
    public ValidationException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }
}
