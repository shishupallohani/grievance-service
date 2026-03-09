package com.assist.grievance.service;

import com.assist.grievance.data.model.request.GrievancePaginatedRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

public interface GrievanceExportService {

    byte[] exportGrievances(GrievancePaginatedRequest request);
}
