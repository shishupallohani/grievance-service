package com.assist.grievance.service;

import com.assist.grievance.commonUtil.ResponseData;
import com.assist.grievance.commonUtil.SearchResponseData;
import com.assist.grievance.data.model.request.GrievancePaginatedRequest;
import com.assist.grievance.data.model.request.GrievanceRequestDto;
import org.springframework.http.ResponseEntity;

public interface GrievanceService {

    ResponseEntity<ResponseData> receivedGrievance(GrievanceRequestDto request);

    ResponseEntity<ResponseData> updateGrievance(GrievanceRequestDto request);

    ResponseEntity<ResponseData> getGrievanceByAckNo(String ackNo);

    ResponseEntity<SearchResponseData<GrievanceRequestDto>> findPaginatedGrievance(GrievancePaginatedRequest request);

    ResponseEntity<ResponseData> getGrievanceSummary();




}
