package com.assist.grievance.service;

import com.assist.grievance.commonUtil.ResponseData;
import com.assist.grievance.commonUtil.SearchResponseData;
import com.assist.grievance.data.model.request.GrievanceRegistrationDTO;
import com.assist.grievance.data.model.request.GrievanceTransactionDto;
import com.assist.grievance.data.model.request.RegisterGrievancePaginatedRequest;
import com.assist.grievance.data.model.request.RegisterTxnGrievancePageRequest;
import org.springframework.http.ResponseEntity;

public interface GrievanceRegistrationService {

    ResponseEntity<ResponseData> saveNewGrievance(GrievanceRegistrationDTO dto);

    ResponseEntity<ResponseData> updateRegisterGrievance(GrievanceRegistrationDTO dto);

    ResponseEntity<ResponseData> getRegisterGrievance(String grievanceNumber);

    ResponseEntity<SearchResponseData<GrievanceRegistrationDTO>> findPaginatedRegisterGrievance(RegisterGrievancePaginatedRequest request);

    ResponseEntity<ResponseData> getRegisterGrievanceTxnByGrievanceNo(String grievanceNumber);

    ResponseEntity<SearchResponseData<GrievanceTransactionDto>> findPaginatedRegisterTxnGrievances(RegisterTxnGrievancePageRequest request);


}
