package com.assist.grievance.api.controller;


import com.assist.grievance.commonUtil.CommonUtil;
import com.assist.grievance.commonUtil.ResponseData;
import com.assist.grievance.commonUtil.SearchResponseData;
import com.assist.grievance.data.model.request.GrievancePaginatedRequest;
import com.assist.grievance.data.model.request.GrievanceRequestDto;
import com.assist.grievance.service.GrievanceExportService;
import com.assist.grievance.service.GrievanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/grievance")
@Tag(name = "GRIEVANCE MANAGEMENT {ONLINE(via MAIL and PORTAL) AND OFFLINE HANDLED}")
@AllArgsConstructor
@Slf4j
public class GrievanceController {

    private final GrievanceService grievanceService;
    private final GrievanceExportService grievanceExportService;

    // Received Grievances From All Mode
    @PostMapping("/register")
    @Operation(summary = "Received Grievances", description = "This endpoint received a Grievances From all Mode if not already exist. All the field of request body is required.")
    public ResponseEntity<ResponseData> receivedGrievance(@Valid @RequestBody GrievanceRequestDto request, BindingResult bindingResult) {
        log.info("Inside GrievanceController >> createGrievance");
        return bindingResult.hasErrors() ? CommonUtil.generateBindingResultResponse(bindingResult) : grievanceService.receivedGrievance(request);

    }


    @PutMapping("/update")
    @Operation(summary = "update Grievance", description = "This endpoint updates a Grievance. All the field of request body is required.")
    public ResponseEntity<ResponseData> updateGrievance(@Valid @RequestBody GrievanceRequestDto request, BindingResult bindingResult) {
        log.info("Inside GrievanceController >> updateGrievance");
        return bindingResult.hasErrors() ? CommonUtil.generateBindingResultResponse(bindingResult) : grievanceService.updateGrievance(request);

    }


    @GetMapping("/acknowledgement/{ackNo}")
    @Operation(summary = "Get Grievance by Acknowledgement Number", description = "This endpoint retrieves a Grievance using the provided Acknowledgement Number.")
    public ResponseEntity<ResponseData> getGrievanceByAckNo(@PathVariable("ackNo") String ackNo) {
        log.info("Inside GrievanceController >> getGrievanceByAckNo");
        return grievanceService.getGrievanceByAckNo(ackNo);
    }


    @GetMapping("/paginated-search")
    @SecurityRequirement(name = "basicAuth")
    @Operation(summary = "Search Grievances with Pagination", description = "This endpoint allows searching for Grievances with pagination support based on the provided search criteria.")
    public ResponseEntity<SearchResponseData<GrievanceRequestDto>> searchGrievances(@Valid @ModelAttribute GrievancePaginatedRequest searchRequest) {
        log.info("Inside GrievanceController >> searchGrievances");
        return grievanceService.findPaginatedGrievance(searchRequest);
    }


    @GetMapping("/summary")
    @Operation(summary = "Get Grievance Summary", description = "This endpoint returns mode-wise grievance summary (total, processed, pending)")
    public ResponseEntity<ResponseData> getGrievanceSummary() {
        log.info("Inside GrievanceController >> getGrievanceSummary");
        return grievanceService.getGrievanceSummary();
    }


    @GetMapping("/export")
    public ResponseEntity<byte[]> exportGrievances(@ModelAttribute GrievancePaginatedRequest request) {

        byte[] fileData = grievanceExportService.exportGrievances(request);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=grievances.xlsx")
                .contentType(MediaType.parseMediaType(
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(fileData);
    }
}
