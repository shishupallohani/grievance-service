package com.assist.grievance.data.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class GrievanceRequestDto {
    private String acknowledgementNo;
    private String grievanceNo;
    private String emailId;
    private String emailSubject;
    private String emailBody;
    private String insuranceCompany;
    private String receivedFrom;
    private String receivedVia;
    private List<DocumentDto> documents;
    @NotBlank(message = "mode is mandatory")
    private String mode;
    private String grievanceType;
    private String status;
    private String claimNo;
    private String branchName;
    private String department;
    private String inwardType;
    private String name;
    private String mobileNumber;
    private String addressField;
    private String pincode;
    private String locality;
    private String district;
    private String city;
    private String state;
    private String country;
    private String podNo;
    private String documentType;
    private Integer noOfDocuments;

}
