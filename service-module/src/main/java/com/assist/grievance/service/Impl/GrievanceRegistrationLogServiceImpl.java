package com.assist.grievance.service.Impl;


import com.assist.grievance.Util.SecurityUtils;
import com.assist.grievance.data.entity.GrievanceRegistrationLogEntity;
import com.assist.grievance.data.model.request.GrievanceRegistrationLogDto;
import com.assist.grievance.data.repository.GrievanceRegistrationLogRepository;
import com.assist.grievance.service.GrievanceRegistrationLogService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
@Slf4j
public class GrievanceRegistrationLogServiceImpl implements GrievanceRegistrationLogService {

    private final GrievanceRegistrationLogRepository GrievanceRegistrationLogRepository;

    @Override
    public GrievanceRegistrationLogDto saveGrievanceRegistrationLog(GrievanceRegistrationLogDto request) {
        log.info("GrievanceRegistrationLogServiceImpl saveGrievanceRegistrationLog");
        try {
            // Map DTO to Entity
            GrievanceRegistrationLogEntity entity = mapDtoToEntity(request);
            entity.setCreatedBy(SecurityUtils.getAuthenticatedUsername());
            entity.setCreatedDate(LocalDateTime.now());
            // Save Entity
            GrievanceRegistrationLogEntity savedEntity = GrievanceRegistrationLogRepository.save(entity);
            // Map Entity back to DTO
            return mapEntityToDto(savedEntity);
        } catch (Exception e) {
            log.error("GrievanceRegistrationLogServiceImpl saveGrievanceRegistrationLog", e);
        }
        return null;
    }


    private GrievanceRegistrationLogEntity mapDtoToEntity(GrievanceRegistrationLogDto dto) {
        return GrievanceRegistrationLogEntity.builder()
                .departmentName(dto.getDepartmentName())
                .grievanceNumber(dto.getGrievanceNumber())
                .grievance_txn_no(dto.getGrievance_txn_no())
                .acknowledgementNo(dto.getAcknowledgementNo())
                .grievanceType(dto.getGrievanceType())
                .grievanceSubType(dto.getGrievanceSubType())
                .status(dto.getStatus())
                .build();
    }

    private GrievanceRegistrationLogDto mapEntityToDto(GrievanceRegistrationLogEntity entity) {
        return GrievanceRegistrationLogDto.builder()
                .departmentName(entity.getDepartmentName())
                .grievanceNumber(entity.getGrievanceNumber())
                .grievance_txn_no(entity.getGrievance_txn_no())
                .acknowledgementNo(entity.getAcknowledgementNo())
                .grievanceType(entity.getGrievanceType())
                .grievanceSubType(entity.getGrievanceSubType())
                .status(entity.getStatus())
                .build();
    }

}