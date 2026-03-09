package com.assist.grievance.data.model.request;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class RegisterGrievancePaginatedRequest extends HitpaBasePageRequest{
    private String grievanceType;
    private String insuranceCompanyName;
    private String policyType;
    private String receivedFrom;
    private String grievanceNumber;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime fromDate;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime toDate;
}
