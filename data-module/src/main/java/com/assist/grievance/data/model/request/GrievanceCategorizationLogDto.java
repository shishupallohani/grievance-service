package com.assist.grievance.data.model.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GrievanceCategorizationLogDto {
    private String grievanceNumber;
    private String grievanceCategory;
    private String departmentName;
    private String caseNumber;
    private String claimNo;
    private String policyNo;
}
