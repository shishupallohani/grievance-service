package com.assist.grievance.data.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class GrievanceRegistrationLogDto {
    private String departmentName;
    private String grievanceNumber;
    private String grievance_txn_no;
    private String acknowledgementNo;
    private String grievanceType;
    private String grievanceSubType;
    private String status;

}

