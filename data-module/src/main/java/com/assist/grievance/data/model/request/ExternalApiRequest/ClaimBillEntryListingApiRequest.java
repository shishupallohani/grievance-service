package com.assist.grievance.data.model.request.ExternalApiRequest;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ClaimBillEntryListingApiRequest {
    private int page;
    private int size;
    private String agentCode;
    private String claimNoExt;
    private String billEntryPickedBy;
    private String providerName;
    private List<String> claimType;
    private String patientName;
    private String corporateId;
    private String typeOfAdmission;
    private String admissionDate;
    private String dischargeDate;
    private String claimedAmount;
    private String billingOfficeTpa;
    private Boolean summary;
    private Boolean active;
}
