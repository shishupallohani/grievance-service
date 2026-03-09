package com.assist.grievance.data.model.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GrievanceTransactionDto {
    private String acknowledgementNo;
    private String grievanceNumber;
    private String grievanceTxnNo;
    private String department;
    private String claimNo;
    private String insuranceCompany;
    private String grievanceType;
    private String policyType;
    private String status;
    private String mode;
}
