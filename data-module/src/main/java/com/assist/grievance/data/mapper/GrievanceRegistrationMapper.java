package com.assist.grievance.data.mapper;

import com.assist.grievance.data.entity.GrievanceRegistrationEntity;
import com.assist.grievance.data.model.request.GrievanceRegistrationDTO;
import com.assist.grievance.data.model.request.GrievanceRegistrationLogDto;
import org.springframework.stereotype.Component;

@Component
public class GrievanceRegistrationMapper {

    public GrievanceRegistrationEntity mapToEntity(GrievanceRegistrationDTO dto) {
        return GrievanceRegistrationEntity.builder()
                // ===== New Grievance =====
                .receivedFrom(dto.getReceivedFrom())
                .acknowledgementNo(dto.getAcknowledgementNo())
                .othersName(dto.getOthersName())
                .insuranceCompanyName(dto.getInsuranceCompanyName())
                .grievanceType(dto.getGrievanceType())
                .grievanceSubType(dto.getGrievanceSubType())
                .grievanceDetailType(dto.getGrievanceDetailType())
                .policyType(dto.getPolicyType())
                .policyNumber(dto.getPolicyNumber())
                .claimNumber(dto.getClaimNumber())
                .insuredName(dto.getInsuredName())
                .phoneNumber(dto.getPhoneNumber())
                .senderEmail(dto.getSenderEmail())
                .senderAddress(dto.getSenderAddress())
                .status("registered")
                .department(dto.getDepartment())
                .mode(dto.getMode())
                // ===== Document =====
                .documentName(dto.getDocumentName())
                .documentPath(dto.getDocumentPath())
                // ===== Remarks =====
                .remarks(dto.getRemarks())
                // ===== Email Section =====
                .emailTo(dto.getEmailTo())
                .emailCc(dto.getEmailCc())
                .emailBcc(dto.getEmailBcc())
                .build();
    }

    public GrievanceRegistrationDTO mapToDto(GrievanceRegistrationEntity entity) {
        return GrievanceRegistrationDTO.builder()
                // ===== New Grievance =====
                .receivedFrom(entity.getReceivedFrom())
                .acknowledgementNo(entity.getAcknowledgementNo())
                .othersName(entity.getOthersName())
                .insuranceCompanyName(entity.getInsuranceCompanyName())
                .grievanceType(entity.getGrievanceType())
                .grievanceSubType(entity.getGrievanceSubType())
                .grievanceDetailType(entity.getGrievanceDetailType())
                .policyType(entity.getPolicyType())
                .policyNumber(entity.getPolicyNumber())
                .claimNumber(entity.getClaimNumber())
                .insuredName(entity.getInsuredName())
                .phoneNumber(entity.getPhoneNumber())
                .senderEmail(entity.getSenderEmail())
                .senderAddress(entity.getSenderAddress())
                .grievanceNumber(entity.getGrievanceNumber())
                .department(entity.getDepartment())
                // ===== Document =====
                .documentName(entity.getDocumentName())
                .documentPath(entity.getDocumentPath())
                // ===== Remarks =====
                .remarks(entity.getRemarks())
                // ===== Email Section =====
                .emailTo(entity.getEmailTo())
                .emailCc(entity.getEmailCc())
                .emailBcc(entity.getEmailBcc())
                .build();
    }


    public GrievanceRegistrationLogDto mapEntityToLogDto(GrievanceRegistrationEntity entity) {
        return GrievanceRegistrationLogDto.builder()
                .departmentName(entity.getDepartment())
                .grievanceNumber(entity.getGrievanceNumber())
                .grievance_txn_no(entity.getGrievanceTxnNo())
                .acknowledgementNo(entity.getAcknowledgementNo())
                .grievanceType(entity.getGrievanceType())
                .grievanceSubType(entity.getGrievanceSubType())
                .status(entity.getStatus())
                .build();
    }
}
