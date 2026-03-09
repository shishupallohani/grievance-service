package com.assist.grievance.service.Impl;

import com.assist.grievance.Util.SecurityUtils;
import com.assist.grievance.data.entity.GrievanceCategorizationLogEntity;
import com.assist.grievance.data.model.request.GrievanceCategorizationLogDto;
import com.assist.grievance.data.repository.GrievanceCategorizationLogRepository;
import com.assist.grievance.service.GrievanceCategorizationLogService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
@AllArgsConstructor
public class GrievanceCategorizationLogServiceImpl implements GrievanceCategorizationLogService {

    private final GrievanceCategorizationLogRepository repository;

    @Override
    public GrievanceCategorizationLogDto saveGrievanceCategorizationLog(GrievanceCategorizationLogDto dto) {
        log.info("GrievanceCategorizationLogServiceImpl saveGrievanceCategorizationLog");
        try {
            // Map DTO to Entity
            GrievanceCategorizationLogEntity entity = mapDtoToEntity(dto);
            entity.setCreatedBy(SecurityUtils.getAuthenticatedUsername());
            entity.setCreatedAt(LocalDateTime.now());
            // Save Entity
            GrievanceCategorizationLogEntity savedEntity = repository.save(entity);
            // Map Entity back to DTO
            return mapEntityToDto(savedEntity);
        } catch (Exception e) {
            log.error("Error in saveGrievanceCategorizationLog: {}", e.getMessage());
        }
        return null;
    }

    private GrievanceCategorizationLogEntity mapDtoToEntity(GrievanceCategorizationLogDto dto) {
        return GrievanceCategorizationLogEntity.builder()
                .grievanceNumber(dto.getGrievanceNumber())
                .grievanceCategory(dto.getGrievanceCategory())
                .departmentName(dto.getDepartmentName())
                .caseNumber(dto.getCaseNumber())
                .claimNo(dto.getClaimNo())
                .policyNo(dto.getPolicyNo())
                .build();
    }


    private GrievanceCategorizationLogDto mapEntityToDto(GrievanceCategorizationLogEntity entity) {
        return GrievanceCategorizationLogDto.builder()
                .grievanceNumber(entity.getGrievanceNumber())
                .grievanceCategory(entity.getGrievanceCategory())
                .departmentName(entity.getDepartmentName())
                .caseNumber(entity.getCaseNumber())
                .claimNo(entity.getClaimNo())
                .policyNo(entity.getPolicyNo())
                .build();
    }
}
