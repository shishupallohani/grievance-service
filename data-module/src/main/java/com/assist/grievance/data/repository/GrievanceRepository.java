package com.assist.grievance.data.repository;


import com.assist.grievance.data.entity.GrievanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GrievanceRepository extends JpaRepository<GrievanceEntity, Integer>, JpaSpecificationExecutor<GrievanceEntity> {

    Optional<GrievanceEntity> findByAcknowledgementNoAndStatusIsNotNullAndStatus(String acknowledgementNo, String status);


    @Query(value = """
                   SELECT
                       mode_label AS mode,
                       COUNT(*) FILTER (WHERE status = 'received') AS total_count,
                       COUNT(*) FILTER (WHERE status = 'registered') AS processed_count,
                       COUNT(*) FILTER (WHERE status = 'received')
                         - COUNT(*) FILTER (WHERE status = 'registered') AS pending_count
                   FROM (
                       SELECT
                           CASE\s
                               WHEN UPPER(TRIM(mode)) = 'ONLINE' THEN 'ONLINE REGISTRATION'
                               WHEN UPPER(TRIM(mode)) = 'OFFLINE' THEN 'MANUAL REGISTRATION'
                               WHEN UPPER(TRIM(mode)) = 'PORTAL' THEN 'PORTAL REGISTRATION'
                           END AS mode_label,
                           status
                       FROM grievance
                       WHERE mode IS NOT NULL
                         AND TRIM(mode) <> ''
                         AND UPPER(TRIM(mode)) IN ('ONLINE','PORTAL','OFFLINE')
                   )
                   GROUP BY mode_label;
            
            """, nativeQuery = true)
    List<Object[]> fetchGrievanceSummary();
}
