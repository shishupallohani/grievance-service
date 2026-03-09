package com.assist.grievance.exception;

import org.springframework.http.HttpStatus;

public class RecordsNotFoundException extends BusinessException{
    public RecordsNotFoundException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }
}
