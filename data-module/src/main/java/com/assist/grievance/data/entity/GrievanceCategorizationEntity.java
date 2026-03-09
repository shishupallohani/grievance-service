package com.assist.grievance.data.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "grievance_categorization")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GrievanceCategorizationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    // -------- Grievance Details --------

    @Column(name = "grievance_no")
    private String grievanceNumber;

    @Column(name = "grievance_category")
    private String grievanceCategory;

    @Column(name = "grievance_severity")
    private String grievanceSeverity;

    @Column(name = "remarks")
    private String remarks;

    @Column(name = "email_to")
    private String emailTo;

    @Column(name = "email_cc")
    private String emailCc;

    @Column(name = "email_bcc")
    private String emailBcc;

    @Column(name = "legal_category")
    private String legalCategory;

    @Column(name = "legal_sub_category")
    private String legalSubCategory;

    @Column(name = "legal_sub_type_category")
    private String legalSubTypeCategory;

    @Column(name = "name_of_court")
    private String nameOfCourt;

    @Column(name = "copy_of_fir", columnDefinition = "TEXT")
    private String copyOfFir;

    @Column(name = "police_station_address")
    private String policeStationAddress;

    @Column(name = "name_of_sho")
    private String nameOfSho;

    @Column(name = "eye_witness_names", columnDefinition = "TEXT")
    private String eyeWitnessNames;

    @Column(name = "date_of_receipt_of_complaint")
    private LocalDateTime dateOfReceiptOfComplaint;

    @Column(name = "case_number")
    private String caseNumber;

    @Column(name = "mode_of_complaint")
    private String modeOfComplaint;

    @Column(name = "department")
    private String departmentName;

    @Column(name = "name_and_address_of_complainant", columnDefinition = "TEXT")
    private String nameAndAddressOfComplainant;

    @Column(name = "summons_served_date_time")
    private LocalDateTime summonsServedDateTime;

    @Column(name = "summons_received_by_name")
    private String summonsReceivedByName;

    @Column(name = "relationship_with_policy_holder")
    private String relationshipWithPolicyHolder;

    @Column(name = "date_of_loss")
    private LocalDateTime dateOfLoss;

    @Column(name = "date_of_first_hearing")
    private LocalDateTime dateOfFirstHearing;

    @Column(name = "name_and_address_of_insurance_company", columnDefinition = "TEXT")
    private String nameAndAddressOfInsuranceCompany;

    @Column(name = "insured_name")
    private String insuredName;

    @Column(name = "patient_name")
    private String patientName;

    @Column(name = "patient_gender")
    private String patientGender;

    @Column(name = "patient_age")
    private Integer patientAge;

    @Column(name = "mobile_no")
    private String mobileNo;

    @Column(name = "valid_email_id")
    private String validEmailId;

    @Column(name = "valid_id_proof")
    private String validIdProof;

    @Column(name = "valid_address_proof")
    private String validAddressProof;

    @Column(name = "complete_address_of_insured_with_pincode", columnDefinition = "TEXT")
    private String completeAddressOfInsuredWithPincode;

    @Column(name = "claim_no")
    private String claimNo;

    @Column(name = "policy_no")
    private String policyNo;

    @Column(name = "policy_product_name_with_uin_no")
    private String policyProductNameWithUinNo;

    @Column(name = "copy_of_insurance_policy", columnDefinition = "TEXT")
    private String copyOfInsurancePolicy;

    @Column(name = "type_of_complaint")
    private String typeOfComplaint;

    @Column(name = "complaint_details_in_brief", columnDefinition = "TEXT")
    private String complaintDetailsInBrief;

    @Column(name = "copy_of_complaint_to_insurance_company", columnDefinition = "TEXT")
    private String copyOfComplaintToInsuranceCompany;

    @Column(name = "reply_from_insurance_company", columnDefinition = "TEXT")
    private String replyFromInsuranceCompany;

    @Column(name = "complaint_to_insurance_ombudsman", columnDefinition = "TEXT")
    private String complaintToInsuranceOmbudsman;

    @Column(name = "reply_from_insurance_ombudsman", columnDefinition = "TEXT")
    private String replyFromInsuranceOmbudsman;

    @Column(name = "proof_of_premium_payment", columnDefinition = "TEXT")
    private String proofOfPremiumPayment;

    //new fields for grievance categorization
    @Column(name = "grievance_category_code")
    private String grievanceCategoryCode;

    @Column(name = "remarks_legal")
    private String remarksLegal;

    @Column(name = "acknowledgement_no")
    private String acknowledgementNo;

    @Column(name = "grievance_severity_code")
    private String grievanceSeverityCode;

    @Column(name = "summons_served_details", columnDefinition = "TEXT")
    private String summonsServedDetails;

    @Column(name = "patient_details", columnDefinition = "TEXT")
    private String patientDetails;

    @Column(name = "legal_category_code")
    private String legalCategoryCode;

    @Column(name = "legal_sub_category_code")
    private String legalSubCategoryCode;

    @Column(name = "policy_type")
    private String policyType;

    @Column(name = "grievance_type")
    private String grievanceType;

    @Column(name = "received_type")
    private String receivedType;

    @Column(name = "created_on")
    private LocalDateTime createdAt;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "updated_by")
    private String updatedBy;

    @OneToMany(
            mappedBy = "grievanceCase",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            orphanRemoval = true
    )
    private List<DocumentEntity> document;
}
