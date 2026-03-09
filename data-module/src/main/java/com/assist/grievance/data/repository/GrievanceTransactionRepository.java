package com.assist.grievance.data.repository;


import com.assist.grievance.data.entity.GrievanceTransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GrievanceTransactionRepository extends JpaRepository<GrievanceTransactionEntity, Integer>, JpaSpecificationExecutor<GrievanceTransactionEntity> {

    Optional<GrievanceTransactionEntity> findByGrievanceNumber(String grievanceNumber);

    List<GrievanceTransactionEntity> findAllByGrievanceNumber(String grievanceNumber);
}
