package com.assist.grievance.commonUtil;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExternalClaimApiResponse <T>{
        private String status;
        private String messages;
        private T data;
}
