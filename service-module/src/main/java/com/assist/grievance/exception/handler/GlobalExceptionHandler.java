package com.assist.grievance.exception.handler;

import com.assist.grievance.commonUtil.CommonUtil;
import com.assist.grievance.commonUtil.Constants;
import com.assist.grievance.commonUtil.ResponseData;
import com.assist.grievance.exception.BusinessException;
import com.assist.grievance.exception.ExportException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseData> handleGenericException(Exception ex) {
        log.error("Unhandled exception occurred", ex);
        return CommonUtil.generateExceptionResponse(ex);
    }
}
