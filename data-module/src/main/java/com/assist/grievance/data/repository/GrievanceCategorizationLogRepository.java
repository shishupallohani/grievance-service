package com.assist.grievance.data.repository;


import com.assist.grievance.data.entity.GrievanceCategorizationLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GrievanceCategorizationLogRepository extends JpaRepository<GrievanceCategorizationLogEntity,Integer> {
}
