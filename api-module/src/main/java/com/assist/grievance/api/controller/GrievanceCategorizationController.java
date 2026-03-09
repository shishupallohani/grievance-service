package com.assist.grievance.api.controller;


import com.assist.grievance.commonUtil.CommonUtil;
import com.assist.grievance.commonUtil.ResponseData;
import com.assist.grievance.commonUtil.SearchResponseData;
import com.assist.grievance.data.model.request.GrievanceCategorizationDto;
import com.assist.grievance.data.model.request.GrievanceCategorizationPaginatedRequest;
import com.assist.grievance.service.GrievanceCategorizationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/grievance-categorization")
@Tag(name = "GRIEVANCE CATEGORIZATION {CATEGORIZATION OF GRIEVANCE}")
@AllArgsConstructor
@Slf4j
public class GrievanceCategorizationController {

    private final GrievanceCategorizationService grievanceCategorizationService;


    @PostMapping("/save")
    @Operation(summary = "Save Grievance Categorization", description = "This endpoint saves the categorization details of a grievance.")
    public ResponseEntity<ResponseData> saveGrievanceCategorization(@Valid @RequestBody GrievanceCategorizationDto dto, BindingResult bindingResult) {
        log.info("Inside GrievanceCategorizationController >> saveGrievanceCategorization");
        return bindingResult.hasErrors() ? CommonUtil.generateBindingResultResponse(bindingResult) : grievanceCategorizationService.saveGrievanceCategorization(dto);
    }

    @GetMapping("/fetch-paginated")
    @Operation(summary = "Fetch Paginated Grievance Categorization Records", description = "This endpoint retrieves paginated records of grievance categorizations based on the provided filtering and pagination criteria.")
    public ResponseEntity<SearchResponseData<GrievanceCategorizationDto>> fetchPaginatedGrievanceCategorization(@Valid @ModelAttribute GrievanceCategorizationPaginatedRequest request) {
        log.info("Inside GrievanceCategorizationController fetchPaginatedGrievanceCategorization");
        return grievanceCategorizationService.getPaginatedCategorizationData(request);
    }


    @GetMapping("/grievance-no/{grievanceNo}")
    @Operation(summary = "Get Grievance Categorization by Grievance Number", description = "This endpoint retrieves the categorization details of a grievance using the provided Grievance Number.")
    public ResponseEntity<ResponseData> getGrievanceByGrievanceNo(@PathVariable("grievanceNo") String grievanceNo) {
        log.info("Inside GrievanceCategorizationController >> getGrievanceByGrievanceNo");
        return grievanceCategorizationService.getCategorizationByGrievanceNo(grievanceNo);
    }

}
