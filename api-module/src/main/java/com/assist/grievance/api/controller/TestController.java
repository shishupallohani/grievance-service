package com.assist.grievance.api.controller;


import com.assist.grievance.api.ExternalApi.ClaimsFiegnClientService;
import com.assist.grievance.api.ExternalApi.DmsFiegnClientService;
import com.assist.grievance.commonUtil.ExternalClaimApiResponse;
import com.assist.grievance.data.model.request.ExternalApiRequest.ClaimBillEntryListingApiRequest;
import com.assist.grievance.data.model.response.BillEntryPageData;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/test-controller")
@Tag(name = "EXTERNAL API TEST CONTROLLER")
@AllArgsConstructor
@Slf4j
public class TestController {

    private final DmsFiegnClientService dmsFeignClientService;
    private final ClaimsFiegnClientService claimsFeignClientService;


    @PostMapping(value = "/preauth/inward/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> testUpload(@RequestPart("file") MultipartFile file) {
        return dmsFeignClientService.uploadDocument(file, "internal", "CLCER_004");
    }




    @PostMapping("/bill-listing/{page}/{size}")
    public ResponseEntity<ExternalClaimApiResponse<BillEntryPageData>> testBillListing(@PathVariable Integer page, @PathVariable Integer size) {

        ClaimBillEntryListingApiRequest request= ClaimBillEntryListingApiRequest.builder()
                .page(page)
                .size(size)
                .claimType(List.of("REIMBURSEMENT", "CASHLESS"))
                .summary(Boolean.TRUE)
                .active(Boolean.TRUE)
                .build();

        return claimsFeignClientService.searchBillEntryPaginatedData(request);
    }




}
