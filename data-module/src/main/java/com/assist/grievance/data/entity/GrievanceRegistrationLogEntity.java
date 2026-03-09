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
@Table(name = "grievance_registration_log")
public class GrievanceRegistrationLogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="grievance_no")
    private String grievanceNumber;

    @Column(name="grievance_txn_no")
    private String grievance_txn_no;

    @Column(name="acknowledgement_no")
    private String acknowledgementNo;

    @Column(name = "department_name")
    private String departmentName;

    @Column(name = "grievance_type")
    private String grievanceType;

    @Column(name = "grievance_sub_type")
    private String grievanceSubType;

    @Column(name="status")
    private String status;

    @Column(name = "created_on")
    private LocalDateTime createdDate;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name="updated_by")
    private String updatedBy;

    @Column(name="updated_at")
    private LocalDateTime updatedAt;



}
