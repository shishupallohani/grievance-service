package com.assist.grievance.service;

import com.assist.grievance.commonUtil.ResponseData;
import com.assist.grievance.data.model.request.GrievanceTransactionDto;
import org.springframework.http.ResponseEntity;

public interface GrievanceTransactionService {

    ResponseEntity<ResponseData> saveGrievanceTransaction(GrievanceTransactionDto grievanceTransactionDto);

}
