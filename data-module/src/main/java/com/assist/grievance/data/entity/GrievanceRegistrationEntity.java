package com.assist.grievance.data.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "grievance_registration")
public class GrievanceRegistrationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // ===== New Grievance =====

    @Column(name = "received_from")
    private String receivedFrom;

    @Column(name = "others_name")
    private String othersName;

    @Column(name = "insurance_company_name")
    private String insuranceCompanyName;

    @Column(name = "grievance_type")
    private String grievanceType;

    @Column(name = "grievance_sub_type")
    private String grievanceSubType;

    @Column(name = "grievance_detail_type")
    private String grievanceDetailType;

    @Column(name = "policy_type")
    private String policyType;

    @Column(name = "policy_number")
    private String policyNumber;

    @Column(name = "claim_number")
    private String claimNumber;

    @Column(name = "insured_name")
    private String insuredName;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "sender_email")
    private String senderEmail;

    @Column(name = "sender_address")
    private String senderAddress;

    @Column(name = "department")
    private String department;

    @Column(name = "created_on")
    private LocalDateTime createdDate;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name="updated_by")
    private String updatedBy;

    @Column(name="updated_at")
    private LocalDateTime updatedAt;

    // ===== Document =====
    @Column(name = "document_name")
    private String documentName;

    @Column(name = "document_path")
    private String documentPath;

    // ===== Remarks =====
    @Column(name = "remarks")
    private String remarks;

    // ===== Email Section =====
    @Column(name = "email_to")
    private String emailTo;

    @Column(name = "email_cc")
    private String emailCc;

    @Column(name = "email_bcc")
    private String emailBcc;

    @Column(name="status")
    private String status;

    @Column(name="grievance_no")
    private String grievanceNumber;

    @Column(name="acknowledgement_no")
    private String acknowledgementNo;

    @Column(name="grievance_txn_no")
    private String grievanceTxnNo;

    @Column(name="mode")
    private String mode;
}
