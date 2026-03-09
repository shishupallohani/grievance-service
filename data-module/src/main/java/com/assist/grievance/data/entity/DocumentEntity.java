package com.assist.grievance.data.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "document_store")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "grievance_number")
    private String grievanceNumber;

    @Column(name="acknowledgement_number")
    private String acknowledgementNumber;

    // FIR_COPY, VALID_ID_PROOF, etc
    @Column(name = "document_type")
    private String documentType;

    @Column(name = "document_name")
    private String name;

    @Column(name = "document_data")
    private String data;

    @Column(name = "updated_by")
    private LocalDateTime uploadedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grievance_case_id")
    private GrievanceCategorizationEntity grievanceCase;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grievance_id")
    private GrievanceEntity grievance;


}
