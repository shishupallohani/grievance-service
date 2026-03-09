package com.assist.grievance.api.ExternalApi;


import com.assist.grievance.commonUtil.ExternalClaimApiResponse;
import com.assist.grievance.data.model.request.ExternalApiRequest.ClaimBillEntryListingApiRequest;
import com.assist.grievance.data.model.response.BillEntryPageData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "claimsFiegnClientService", url = "${CLAIM_EXTERNAL_BASE_URL:http://localhost}")
public interface ClaimsFiegnClientService {



    @PostMapping(value ="/bill-entry-dashboard/bill-listing" , consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ExternalClaimApiResponse<BillEntryPageData>> searchBillEntryPaginatedData(@RequestBody ClaimBillEntryListingApiRequest request);

}
