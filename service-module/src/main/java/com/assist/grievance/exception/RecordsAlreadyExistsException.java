package com.assist.grievance.exception;

import org.springframework.http.HttpStatus;

public class RecordsAlreadyExistsException extends BusinessException{
    public RecordsAlreadyExistsException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }
}
