package com.assist.grievance.data.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


/*
 *   Track all export requests in database
 * - Audit trail (who requested what and when)
 * - Job status tracking (RECEIVED/PROCESSED/PENDING)
 * - File path storage for download
 * - Error message storage for debugging
 */
@Entity
@Table(name = "export_job")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExportJobEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    // Current status: RECEIVED/PROCESSED/PENDING
    @Column(name = "status")
    private String status;
    // Store filter criteria as JSON for re-running or debugging
    @Lob
    @Column(name = "filters_json", columnDefinition = "TEXT")
    private String filtersJson;
    // S3 URL or file path where exported file is stored
    @Column(name = "filePath", length = 500)
    private String filePath;
    // User who requested this export (from security context)
    @Column(name = "requested_by", length = 100)
    private String requestedBy;
    // When export was requested
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    // When export completed (success or failure)
    @Column(name = "completed_at")
    private LocalDateTime completedAt;
    // Total records exported (for user info)
    @Column(name = "total_records")
    private Long totalRecords;

}
