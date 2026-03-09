package com.assist.grievance.data.mapper;


import com.assist.grievance.data.entity.GrievanceEntity;
import com.assist.grievance.data.model.request.GrievanceRequestDto;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class GrievanceMapper {

    public GrievanceEntity mapToEntity(GrievanceRequestDto request) {
        return GrievanceEntity.builder()
                .acknowledgementNo(request.getAcknowledgementNo())
                .insuranceCompany(request.getInsuranceCompany())
                .emailSubject(request.getEmailSubject())
                .emailId(request.getEmailId())
                .emailBody(request.getEmailBody())
                .receivedFrom(request.getReceivedFrom())
                .grievanceType(request.getGrievanceType())
                .grievanceNo(request.getGrievanceNo())
                .receivedOn(LocalDateTime.now())
                .build();
    }

    public GrievanceRequestDto mapToDto(GrievanceEntity entity) {
        return GrievanceRequestDto.builder()
                .acknowledgementNo(entity.getAcknowledgementNo())
                .insuranceCompany(entity.getInsuranceCompany())
                .emailSubject(entity.getEmailSubject())
                .emailId(entity.getEmailId())
                .emailBody(entity.getEmailBody())
                .receivedFrom(entity.getReceivedFrom())
                .grievanceType(entity.getGrievanceType())
                .grievanceNo(entity.getGrievanceNo())
                .status(entity.getStatus())
                .mode(entity.getMode())
                .city(entity.getCity())
                .addressField(entity.getAddressField())
                .branchName(entity.getBranchName())
                .department(entity.getDepartment())
                .country(entity.getCountry())
                .district(entity.getDistrict())
                .locality(entity.getLocality())
                .inwardType(entity.getInwardType())
                .state(entity.getState())
                .mobileNumber(entity.getMobileNumber())
                .noOfDocuments(entity.getNoOfDocuments())
                .pincode(entity.getPincode())
                .podNo(entity.getPodNo())
                .name(entity.getName())
                .documentType(entity.getDocumentType())
                .receivedVia(entity.getReceivedVia())
                .build();
    }
}
