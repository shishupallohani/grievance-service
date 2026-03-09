package com.assist.grievance.api.ExternalApi;


import com.assist.grievance.api.config.FeignMultipartConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(name = "dmsFiegnClientService", url = "${DMS_EXTERNAL_BASE_URL:http://localhost}", configuration = FeignMultipartConfig.class)
public interface DmsFiegnClientService {


    @PostMapping(value = "/preauth/inward/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<Object> uploadDocument(@RequestPart("file") MultipartFile file, @RequestPart("doc_type") String docType, @RequestPart("doctype_code") String docTypeCode);
}
