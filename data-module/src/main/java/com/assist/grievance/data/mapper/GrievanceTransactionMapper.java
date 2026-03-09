package com.assist.grievance.data.mapper;

import com.assist.grievance.data.entity.GrievanceTransactionEntity;
import com.assist.grievance.data.model.request.GrievanceTransactionDto;
import org.springframework.stereotype.Component;

@Component
public class GrievanceTransactionMapper {

    public GrievanceTransactionEntity mapToEntity(GrievanceTransactionDto dto){
        return GrievanceTransactionEntity.builder()
                .acknowledgementNo(dto.getAcknowledgementNo())
                .claimNo(dto.getClaimNo())
                .grievanceTxnNo(dto.getGrievanceTxnNo())
                .policyType(dto.getPolicyType())
                .mode(dto.getMode())
                .grievanceNumber(dto.getGrievanceNumber())
                .status(dto.getStatus())
                .insuranceCompany(dto.getInsuranceCompany())
                .grievanceType(dto.getGrievanceType())
                .build();
    }


    public GrievanceTransactionDto mapToDto(GrievanceTransactionEntity entity){
        return GrievanceTransactionDto.builder()
                .acknowledgementNo(entity.getAcknowledgementNo())
                .claimNo(entity.getClaimNo())
                .grievanceTxnNo(entity.getGrievanceTxnNo())
                .policyType(entity.getPolicyType())
                .mode(entity.getMode())
                .grievanceNumber(entity.getGrievanceNumber())
                .status(entity.getStatus())
                .insuranceCompany(entity.getInsuranceCompany())
                .grievanceType(entity.getGrievanceType())
                .build();
    }
}
