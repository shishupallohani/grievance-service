package com.assist.grievance.commonUtil;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchResponseData <T>{
    private Object resource;
    private Boolean status;
    private String message;
    private Object data;
    private String error;
}
