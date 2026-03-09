package com.assist.grievance.data.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "grievance_categorization_log")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GrievanceCategorizationLogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "grievance_no")
    private String grievanceNumber;

    @Column(name = "grievance_category")
    private String grievanceCategory;

    @Column(name ="department")
    private String departmentName;

    @Column(name = "case_number")
    private String caseNumber;

    @Column(name = "claim_no")
    private String claimNo;

    @Column(name = "policy_no")
    private String policyNo;

    @Column(name = "created_on")
    private LocalDateTime createdAt;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name="updated_at")
    private LocalDateTime updatedAt;

    @Column(name="updated_by")
    private String updatedBy;

}
