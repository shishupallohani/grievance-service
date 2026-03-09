package com.assist.grievance.data.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GrievanceCategorizationDto {
    private String grievanceNo;
    private String grievanceCategory;
    private String grievanceSeverity;
    private String remarks;
    private String emailTo;
    private String emailCc;
    private String emailBcc;
    private String legalCategory;
    private String legalSubCategory;
    private String legalSubTypeCategory;
    private String nameOfCourt;
    private String policeStationAddress;
    private String nameOfSho;
    private String eyeWitnessNames;
    private LocalDateTime dateOfReceiptOfComplaint;
    private String caseNumber;
    private String modeOfComplaint;
    private String nameAndAddressOfComplainant;
    private LocalDateTime summonsServedDateTime;
    private String summonsReceivedByName;
    private String relationshipWithPolicyHolder;
    private LocalDateTime dateOfLoss;
    private LocalDateTime dateOfFirstHearing;
    private String nameAndAddressOfInsuranceCompany;
    private String insuredName;
    private String patientName;
    private String patientGender;
    private Integer patientAge;
    private String mobileNo;
    private String validEmailId;
    private String completeAddressOfInsuredWithPincode;
    private String claimNo;
    private String policyNo;
    private String policyProductNameWithUinNo;
    private String typeOfComplaint;
    private String complaintDetailsInBrief;
    private String proofOfPremiumPayment;
    private String departmentName;
    private String grievanceCategoryCode;
    private String remarksLegal;
    private String acknowledgementNo;
    private String grievanceSeverityCode;
    private String legalCategoryCode;
    private String legalSubCategoryCode;
    private String patientDetails;
    private String policyType;
    private String grievanceType;
    private String receivedType;
    private List<DocumentDto> copyOfFir;
    private List<DocumentDto> validIdProof;
    private List<DocumentDto> validAddressProof;
    private List<DocumentDto> replyFromInsuranceCompany;
    private List<DocumentDto> complaintToInsuranceOmbudsman;
    private List<DocumentDto> replyFromInsuranceOmbudsman;
    private List<DocumentDto> copyOfInsurancePolicy;
    private List<DocumentDto> copyOfComplaintToInsuranceCompany;

}
