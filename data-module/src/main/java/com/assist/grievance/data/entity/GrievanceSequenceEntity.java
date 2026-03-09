package com.assist.grievance.data.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "grievance_sequence", uniqueConstraints =@UniqueConstraint(name = "uk_base_grievance_number", columnNames = "base_grievance_number"))
@Data
public class GrievanceSequenceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "insurance_company", nullable = false, length = 10)
    private String insuranceCompany;

    @Column(name = "financial_year", nullable = false, length = 4)
    private String financialYear;

    @Column(name = "record_type", nullable = false, length = 10)
    private String recordType; // "COUNTER" or "GRIEVANCE"

    @Column(name = "last_sequence", nullable = false)
    private Integer lastSequence = 0;

    @Column(name = "base_grievance_number", length = 50)
    private String baseGrievanceNumber; // Null for counter rows

    @Column(name = "incidence_count", nullable = false)
    private Integer incidenceCount = 0;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
