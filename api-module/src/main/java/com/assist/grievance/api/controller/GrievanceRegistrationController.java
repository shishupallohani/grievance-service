package com.assist.grievance.api.controller;

import com.assist.grievance.commonUtil.CommonUtil;
import com.assist.grievance.commonUtil.ResponseData;
import com.assist.grievance.commonUtil.SearchResponseData;
import com.assist.grievance.data.model.request.GrievanceRegistrationDTO;
import com.assist.grievance.data.model.request.GrievanceTransactionDto;
import com.assist.grievance.data.model.request.RegisterGrievancePaginatedRequest;
import com.assist.grievance.data.model.request.RegisterTxnGrievancePageRequest;
import com.assist.grievance.service.GrievanceRegistrationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/new-grievance")
@Tag(name = "GRIEVANCE REGISTRATION {REGISTRATION BOTH AUTO AND MANUAL GRIEVANCE}")
@AllArgsConstructor
@Slf4j
public class GrievanceRegistrationController {

    private final GrievanceRegistrationService grievanceRegistrationService;

    @PostMapping("/registration")
    @Operation(summary = "Register New Grievance", description = "This endpoint registers a new Grievance if not already exist. All the fields of the request body are required.")
    public ResponseEntity<ResponseData> registerNewGrievance(@Valid @RequestBody GrievanceRegistrationDTO dto, BindingResult bindingResult) {
        log.info("Inside GrievanceRegistrationController >> registerNewGrievance");
        return bindingResult.hasErrors() ? CommonUtil.generateBindingResultResponse(bindingResult) : grievanceRegistrationService.saveNewGrievance(dto);
    }


    @GetMapping("/paginated-search")
    @Operation(summary = "Search Grievances with Pagination", description = "This endpoint allows searching for Grievances with pagination support based on the provided search criteria.")
    public ResponseEntity<SearchResponseData<GrievanceRegistrationDTO>> searchGrievances(@Valid @ModelAttribute RegisterGrievancePaginatedRequest searchRequest) {
        log.info("Inside GrievanceController >> searchGrievances");
        return grievanceRegistrationService.findPaginatedRegisterGrievance(searchRequest);
    }


    @PutMapping("/update")
    @Operation(summary = "Update Registered Grievance", description = "This endpoint updates a registered Grievance. All the fields of the request body are required.")
    public ResponseEntity<ResponseData> updateRegisterGrievance(@Valid @RequestBody GrievanceRegistrationDTO dto, BindingResult bindingResult) {
        log.info("Inside GrievanceRegistrationController >> updateRegisterGrievance");
        return bindingResult.hasErrors() ? CommonUtil.generateBindingResultResponse(bindingResult) : grievanceRegistrationService.updateRegisterGrievance(dto);
    }


    @GetMapping("/view/{grievanceNumber}")
    @Operation(summary = "Get Registered Grievance", description = "This endpoint retrieves a registered Grievance using the provided Grievance Number.")
    public ResponseEntity<ResponseData> getRegisteredGrievance(@PathVariable("grievanceNumber") String grievanceNumber) {
        log.info("Inside GrievanceRegistrationController >> getRegisteredGrievance");
        return grievanceRegistrationService.getRegisterGrievance(grievanceNumber);
    }


    @GetMapping("/transaction-view/{grievanceNo}")
    @Operation(summary = "Get Registered Grievance Transactions", description = "This endpoint retrieves the transactions of a registered Grievance using the provided Grievance Number.")
    public ResponseEntity<ResponseData> getRegisteredGrievanceTxnsByGrievanceNo(@PathVariable("grievanceNo") String grievanceNo) {
        log.info("Inside GrievanceRegistrationController >> getRegisteredGrievanceTxnsByGrievanceNo");
        return grievanceRegistrationService.getRegisterGrievanceTxnByGrievanceNo(grievanceNo);
    }

    @GetMapping("/transaction-paginated-search")
    @Operation(summary = "Search Grievance Transactions with Pagination", description = "This endpoint allows searching for Grievance Transactions with pagination support based on the provided search criteria.")
    public ResponseEntity<SearchResponseData<GrievanceTransactionDto>> searchTxnGrievances(@Valid @ModelAttribute RegisterTxnGrievancePageRequest searchRequest) {
        log.info("Inside GrievanceController >> searchTxnGrievances");
        return grievanceRegistrationService.findPaginatedRegisterTxnGrievances(searchRequest);
    }
}
