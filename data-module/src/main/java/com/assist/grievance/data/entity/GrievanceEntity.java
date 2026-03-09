package com.assist.grievance.data.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "grievance")
public class GrievanceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "acknowledgement_no")
    private String acknowledgementNo;

    @Column(name = "grievance_no")
    private String grievanceNo;

    @Column(name = "insurance_company")
    private String insuranceCompany;

    @Column(name = "email_subject")
    private String emailSubject;

    @Column(name = "email_id")
    private String emailId;

    @Column(name = "email_body", columnDefinition = "TEXT")
    private String emailBody;

    @Column(name = "received_from")
    private String receivedFrom;

    @Column(name = "received_on")
    private LocalDateTime receivedOn;

    @Column(name = "grievance_type")
    private String grievanceType;

    @Column(name = "status")
    private String status;

    @Column(name = "mode")
    private String mode;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "updated_by")
    private String updatedBy;

    //OFFLINE DETAILS
    // ======================
    // Basic Details
    // ======================

    @Column(name = "branch_name")
    private String branchName;

    @Column(name = "department")
    private String department;

    @Column(name = "inward_type")
    private String inwardType;

    // ======================
    // Sender Information
    // ======================

    @Column(name = "received_via")
    private String receivedVia;

    @Column(name = "sender_name")
    private String name;

    @Column(name = "mobile_number")
    private String mobileNumber;

    /**
     * Store image as: Byte Array
     */
    @Column(name = "capture_image")
    private byte[] captureImage;

    @Column(name = "image_path")
    private String imagePath;

    @Column(name = "address")
    private String addressField;

    @Column(name = "pincode")
    private String pincode;

    @Column(name = "locality")
    private String locality;

    @Column(name = "district")
    private String district;

    @Column(name = "city")
    private String city;

    @Column(name = "state")
    private String state;

    @Column(name = "country")
    private String country;

    @Column(name = "pod_no")
    private String podNo;

    // ======================
    // Document Type
    // ======================

    @Column(name = "document_type")
    private String documentType;

    @Column(name = "no_of_documents")
    private Integer noOfDocuments;

    @Column(name = "claim_no")
    private String claimNo;

    @OneToMany(
            mappedBy = "grievance",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            orphanRemoval = true
    )
    private List<DocumentEntity> document;
}
