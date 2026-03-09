package com.assist.grievance.data.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GrievanceRegistrationDTO {

    // ===== New Grievance =====
    private String acknowledgementNo;
    private String grievanceNumber;
    private String grievanceTxnNo;
    private String receivedFrom;
    private String othersName;
    private String insuranceCompanyName;
    private String grievanceType;
    private String grievanceSubType;
    private String grievanceDetailType;
    private String policyType;
    private String policyNumber;
    private String claimNumber;
    private String insuredName;
    private String phoneNumber;
    private String senderEmail;
    private String senderAddress;
    private String department;
    private String mode;

    // ===== Document =====
    private String documentName;
    private String documentPath;

    // ===== Remarks =====
    private String remarks;

    // ===== Email Section =====
    private String emailTo;
    private String emailCc;
    private String emailBcc;
}
