package com.assist.grievance.commonUtil;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.util.*;

public class CommonUtil {

    public static ResponseEntity<ResponseData> generateBindingResultResponse(BindingResult bindingResult) {
        StringJoiner errorString = new StringJoiner(", ");
        bindingResult.getAllErrors().forEach(error -> {
            errorString.add(error.getDefaultMessage());
        });
        ResponseData responseData = new ResponseData();
        responseData.setMessage(Constants.VALIDATION_ERROR_MESSAGE);
        responseData.setStatus(Constants.VALIDATION_ERROR);
        responseData.setData(Collections.emptyList());
        responseData.setError(errorString.toString());
        return new ResponseEntity<>(responseData, HttpStatus.BAD_REQUEST);
    }

    public static ResponseEntity<ResponseData> generateCreatedResponse(Map<String, Integer> dataMap) {
        ResponseData responseData = new ResponseData();
        responseData.setMessage(Constants.SUCCESS_MESSAGE);
        responseData.setStatus(Constants.SUCCESS);
        responseData.setData(dataMap);
        responseData.setResource(Collections.emptyList());
        return new ResponseEntity<>(responseData, HttpStatus.CREATED);
    }

    public static ResponseEntity<ResponseData> generatedCreatedResponse(Map<String, String> dataMap) {
        ResponseData responseData = ResponseData.builder()
                .message(Constants.SUCCESS_MESSAGE)
                .status(Constants.SUCCESS)
                .data(dataMap)
                .resource(Collections.emptyList())
                .build();
        return new ResponseEntity<>(responseData, HttpStatus.CREATED);
    }

    public static <T> ResponseEntity<ResponseData> generateOkResponse(T data) {
        ResponseData responseData = new ResponseData();
        responseData.setMessage(Constants.SUCCESS_MESSAGE);
        responseData.setStatus(Constants.SUCCESS);
        responseData.setData(data);
        responseData.setResource(Collections.emptyList());

        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    public static ResponseEntity<ResponseData> generateCustomResponse(String message, String status, HttpStatus httpStatus) {
        ResponseData responseData = new ResponseData();
        responseData.setMessage(message);
        responseData.setStatus(status);
        responseData.setData(Collections.emptyList());
        responseData.setResource(Collections.emptyList());
        return new ResponseEntity<>(responseData, httpStatus);
    }

    public static ResponseEntity<ResponseData> generateExceptionResponse(Exception e) {
        ResponseData responseData = new ResponseData();
        responseData.setMessage(Constants.ERROR_MESSAGE);
        responseData.setStatus(Constants.ERROR);
        responseData.setData(Collections.emptyList());
        responseData.setResource(Collections.emptyList());
        responseData.setError(e.getMessage());
        return new ResponseEntity<>(responseData, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static <T> ResponseEntity<SearchResponseData<T>> generateSearchCustomResponse(String message, Boolean status, HttpStatus httpStatus) {
        SearchResponseData<T> responseData = new SearchResponseData<>();
        responseData.setMessage(message);
        responseData.setStatus(status);
        responseData.setData(Collections.emptyList());
        responseData.setResource(Collections.emptyList());
        return new ResponseEntity<>(responseData, httpStatus);
    }

    public static <T> ResponseEntity<SearchResponseData<T>> generateSearchOkResponse(Page<T> data) {
        SearchResponseData<T> responseData = new SearchResponseData<>();
        responseData.setMessage(Constants.SUCCESS_MESSAGE);
        responseData.setStatus(true);
        responseData.setData(data);
        responseData.setResource(Collections.emptyList());
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    public static <T> ResponseEntity<SearchResponseData<T>> generateSearchExceptionResponse(Exception e) {
        SearchResponseData<T> responseData = new SearchResponseData<>();
        responseData.setMessage(Constants.ERROR_MESSAGE);
        responseData.setStatus(false);
        responseData.setData(Collections.emptyList());
        responseData.setResource(Collections.emptyList());
        responseData.setError(e.getMessage());
        return new ResponseEntity<>(responseData, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static <T> ResponseEntity<SearchResponseData<T>> generateSearchBindingResultResponse(BindingResult bindingResult) {
        StringJoiner errorString = new StringJoiner(", ");
        bindingResult.getAllErrors().forEach(error -> {
            errorString.add(error.getDefaultMessage());
        });
        SearchResponseData<T> responseData = new SearchResponseData<>();
        responseData.setMessage(Constants.VALIDATION_ERROR_MESSAGE);
        responseData.setStatus(false);
        responseData.setData(Collections.emptyList());
        responseData.setError(errorString.toString());
        return new ResponseEntity<>(responseData, HttpStatus.BAD_REQUEST);
    }


}
