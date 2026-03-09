package com.assist.grievance.data.repository;

import com.assist.grievance.data.entity.ExportJobEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExportJobRepository extends JpaRepository<ExportJobEntity, Integer> {
}

