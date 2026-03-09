package com.assist.grievance.exception.handler;

import com.assist.grievance.commonUtil.CommonUtil;
import com.assist.grievance.commonUtil.Constants;
import com.assist.grievance.commonUtil.ResponseData;
import com.assist.grievance.exception.BusinessException;
import com.assist.grievance.exception.ExportException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.Collections;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ExportException.class)
    public ResponseEntity<ResponseData> handleExportException(ExportException ex) {

        log.error("ExportException occurred: {}", ex.getMessage());

        ResponseData responseData =ResponseData.builder()
                .status(Constants.ERROR)
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(responseData, ex.getHttpStatus());
    }


    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ResponseData> handleBusinessException(BusinessException ex) {
        log.warn("BusinessException caught: {}", ex.getMessage());
        return CommonUtil.generateCustomResponse(ex.getMessage(), Constants.ERROR, ex.getHttpStatus());
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ResponseData> handleNoResourceFoundException(NoResourceFoundException ex) {
        String resourcePath = ex.getResourcePath();
        if ("apple-touch-icon.png".equals(resourcePath) || "apple-touch-icon-precomposed.png".equals(resourcePath)) {
            log.debug("Optional browser icon not found: {}", resourcePath);
        } else {
            log.warn("Static resource not found: {}", resourcePath);
        }
        return CommonUtil.generateCustomResponse("Resource not found", Constants.ERROR, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseData> handleGenericException(Exception ex) {
        log.error("Unhandled exception occurred", ex);
        return CommonUtil.generateExceptionResponse(ex);
    }
}
