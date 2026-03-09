package com.assist.grievance.data.mapper;

import com.assist.grievance.data.entity.GrievanceCategorizationEntity;
import com.assist.grievance.data.model.request.GrievanceCategorizationDto;
import com.assist.grievance.data.model.request.GrievanceCategorizationLogDto;
import org.springframework.stereotype.Component;

@Component
public class GrievanceCategorizationMapper {

    public GrievanceCategorizationEntity dtoToEntity(GrievanceCategorizationDto dto) {
        return GrievanceCategorizationEntity.builder()
                .grievanceNumber(dto.getGrievanceNo())
                .grievanceCategory(dto.getGrievanceCategory())
                .grievanceSeverity(dto.getGrievanceSeverity())
                .remarks(dto.getRemarks())
                .emailTo(dto.getEmailTo())
                .emailCc(dto.getEmailCc())
                .emailBcc(dto.getEmailBcc())
                .legalCategory(dto.getLegalCategory())
                .legalSubCategory(dto.getLegalSubCategory())
                .legalSubTypeCategory(dto.getLegalSubTypeCategory())
                .nameOfCourt(dto.getNameOfCourt())
                .policeStationAddress(dto.getPoliceStationAddress())
                .nameOfSho(dto.getNameOfSho())
                .eyeWitnessNames(dto.getEyeWitnessNames())
                .dateOfReceiptOfComplaint(dto.getDateOfReceiptOfComplaint())
                .caseNumber(dto.getCaseNumber())
                .modeOfComplaint(dto.getModeOfComplaint())
                .nameAndAddressOfComplainant(dto.getNameAndAddressOfComplainant())
                .summonsServedDateTime(dto.getSummonsServedDateTime())
                .summonsReceivedByName(dto.getSummonsReceivedByName())
                .relationshipWithPolicyHolder(dto.getRelationshipWithPolicyHolder())
                .dateOfLoss(dto.getDateOfLoss())
                .dateOfFirstHearing(dto.getDateOfFirstHearing())
                .nameAndAddressOfInsuranceCompany(dto.getNameAndAddressOfInsuranceCompany())
                .insuredName(dto.getInsuredName())
                .patientName(dto.getPatientName())
                .patientGender(dto.getPatientGender())
                .patientAge(dto.getPatientAge())
                .mobileNo(dto.getMobileNo())
                .validEmailId(dto.getValidEmailId())
                .completeAddressOfInsuredWithPincode(dto.getCompleteAddressOfInsuredWithPincode())
                .claimNo(dto.getClaimNo())
                .policyNo(dto.getPolicyNo())
                .policyProductNameWithUinNo(dto.getPolicyProductNameWithUinNo())
                .typeOfComplaint(dto.getTypeOfComplaint())
                .complaintDetailsInBrief(dto.getComplaintDetailsInBrief())
                .departmentName(dto.getDepartmentName())
                .proofOfPremiumPayment(dto.getProofOfPremiumPayment())
                .policyType(dto.getPolicyType())
                .grievanceType(dto.getGrievanceType())
                .receivedType(dto.getReceivedType())
                .build();
    }

    public GrievanceCategorizationDto entityToDto(GrievanceCategorizationEntity entity) {
        return GrievanceCategorizationDto.builder()
                .grievanceNo(entity.getGrievanceNumber())
                .grievanceCategory(entity.getGrievanceCategory())
                .grievanceSeverity(entity.getGrievanceSeverity())
                .remarks(entity.getRemarks())
                .emailTo(entity.getEmailTo())
                .emailCc(entity.getEmailCc())
                .emailBcc(entity.getEmailBcc())
                .legalCategory(entity.getLegalCategory())
                .legalSubCategory(entity.getLegalSubCategory())
                .legalSubTypeCategory(entity.getLegalSubTypeCategory())
                .nameOfCourt(entity.getNameOfCourt())
                .policeStationAddress(entity.getPoliceStationAddress())
                .nameOfSho(entity.getNameOfSho())
                .eyeWitnessNames(entity.getEyeWitnessNames())
                .dateOfReceiptOfComplaint(entity.getDateOfReceiptOfComplaint())
                .caseNumber(entity.getCaseNumber())
                .modeOfComplaint(entity.getModeOfComplaint())
                .nameAndAddressOfComplainant(entity.getNameAndAddressOfComplainant())
                .summonsServedDateTime(entity.getSummonsServedDateTime())
                .summonsReceivedByName(entity.getSummonsReceivedByName())
                .relationshipWithPolicyHolder(entity.getRelationshipWithPolicyHolder())
                .dateOfLoss(entity.getDateOfLoss())
                .dateOfFirstHearing(entity.getDateOfFirstHearing())
                .nameAndAddressOfInsuranceCompany(entity.getNameAndAddressOfInsuranceCompany())
                .insuredName(entity.getInsuredName())
                .patientName(entity.getPatientName())
                .patientGender(entity.getPatientGender())
                .patientAge(entity.getPatientAge())
                .mobileNo(entity.getMobileNo())
                .validEmailId(entity.getValidEmailId())
                .completeAddressOfInsuredWithPincode(entity.getCompleteAddressOfInsuredWithPincode())
                .claimNo(entity.getClaimNo())
                .policyNo(entity.getPolicyNo())
                .policyProductNameWithUinNo(entity.getPolicyProductNameWithUinNo())
                .typeOfComplaint(entity.getTypeOfComplaint())
                .complaintDetailsInBrief(entity.getComplaintDetailsInBrief())
                .proofOfPremiumPayment(entity.getProofOfPremiumPayment())
                .departmentName(entity.getDepartmentName())
                .policyType(entity.getPolicyType())
                .grievanceType(entity.getGrievanceType())
                .receivedType(entity.getReceivedType())
                .build();
    }

    public GrievanceCategorizationLogDto entityToLogDto(GrievanceCategorizationEntity entity) {
        return GrievanceCategorizationLogDto.builder()
                .departmentName(entity.getDepartmentName())
                .grievanceCategory(entity.getGrievanceCategory())
                .grievanceNumber(entity.getGrievanceNumber())
                .caseNumber(entity.getCaseNumber())
                .claimNo(entity.getClaimNo())
                .policyNo(entity.getPolicyNo())
                .build();
    }
}
