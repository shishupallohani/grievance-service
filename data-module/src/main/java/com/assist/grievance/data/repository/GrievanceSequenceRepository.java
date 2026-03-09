package com.assist.grievance.data.repository;

import com.assist.grievance.data.entity.GrievanceSequenceEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface GrievanceSequenceRepository extends JpaRepository<GrievanceSequenceEntity, Long> {

    // Counter row fetch - yeh ensure karega sirf 1 hi counter row ho
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT gs FROM GrievanceSequenceEntity gs " +
            "WHERE gs.insuranceCompany = :company " +
            "AND gs.financialYear = :year " +
            "AND gs.recordType = 'COUNTER'")
    Optional<GrievanceSequenceEntity> findCounterByCompanyAndYearWithLock(
            @Param("company") String company,
            @Param("year") String year
    );

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT gs FROM GrievanceSequenceEntity gs " +
            "WHERE gs.baseGrievanceNumber = :grievanceNumber " +
            "AND gs.recordType = 'GRIEVANCE'")
    Optional<GrievanceSequenceEntity> findGrievanceByNumberWithLock(
            @Param("grievanceNumber") String grievanceNumber
    );
}
