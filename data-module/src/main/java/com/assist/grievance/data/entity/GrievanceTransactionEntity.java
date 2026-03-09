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
@Table(name = "grievance_transaction")
public class GrievanceTransactionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "acknowledgement_no")
    private String acknowledgementNo;

    @Column(name = "grievance_no")
    private String grievanceNumber;

    @Column(name = "grievance_txn_no")
    private String grievanceTxnNo;

    @Column(name = "department")
    private String department;

    @Column(name = "claim_no")
    private String claimNo;

    @Column(name = "insurance_company")
    private String insuranceCompany;

    @Column(name = "grievance_type")
    private String grievanceType;

    @Column(name = "policy_Type")
    private String policyType;

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
}
