package com.assist.grievance.data.repository;

import com.assist.grievance.data.entity.GrievanceRegistrationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GrievanceRegistrationRepository extends JpaRepository<GrievanceRegistrationEntity,Integer>, JpaSpecificationExecutor<GrievanceRegistrationEntity> {

    Optional<GrievanceRegistrationEntity> findByGrievanceNumberAndStatusNotNullAndStatus(String grievanceNumber,String status);
}
