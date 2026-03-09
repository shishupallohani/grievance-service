package com.assist.grievance.exception;

import org.springframework.http.HttpStatus;

public class ExportException extends RuntimeException {

    private final HttpStatus httpStatus;

    public ExportException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
