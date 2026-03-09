package com.assist.grievance.data.repository;

import com.assist.grievance.data.entity.GrievanceRegistrationLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GrievanceRegistrationLogRepository extends JpaRepository<GrievanceRegistrationLogEntity, Integer> {
}
