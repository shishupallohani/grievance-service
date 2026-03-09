package com.assist.grievance.commonUtil;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseData {
    private List<String> resource;
    private String status;
    private String message;
    private Object data;
    private String error;
}
