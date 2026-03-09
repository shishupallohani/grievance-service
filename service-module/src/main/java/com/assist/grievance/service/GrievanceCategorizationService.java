package com.assist.grievance.service;

import com.assist.grievance.commonUtil.ResponseData;
import com.assist.grievance.commonUtil.SearchResponseData;
import com.assist.grievance.data.model.request.GrievanceCategorizationDto;
import com.assist.grievance.data.model.request.GrievanceCategorizationPaginatedRequest;
import org.springframework.http.ResponseEntity;

public interface GrievanceCategorizationService {

    ResponseEntity<ResponseData> saveGrievanceCategorization(GrievanceCategorizationDto dto);

    ResponseEntity<SearchResponseData<GrievanceCategorizationDto>> getPaginatedCategorizationData(GrievanceCategorizationPaginatedRequest request);

    ResponseEntity<ResponseData> getCategorizationByGrievanceNo(String grievanceNo);

}
