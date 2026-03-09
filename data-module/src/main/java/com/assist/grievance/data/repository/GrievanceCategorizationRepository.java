package com.assist.grievance.data.repository;

import com.assist.grievance.data.entity.GrievanceCategorizationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GrievanceCategorizationRepository extends JpaRepository<GrievanceCategorizationEntity, Integer> , JpaSpecificationExecutor<GrievanceCategorizationEntity> {

    Optional<GrievanceCategorizationEntity> findByGrievanceNumber(String grievanceNumber);
}
