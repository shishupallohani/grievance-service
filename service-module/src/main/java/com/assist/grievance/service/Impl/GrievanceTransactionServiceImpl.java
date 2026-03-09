package com.assist.grievance.service.Impl;

import com.assist.grievance.Util.SecurityUtils;
import com.assist.grievance.commonUtil.CommonUtil;
import com.assist.grievance.commonUtil.Constants;
import com.assist.grievance.commonUtil.ResponseData;
import com.assist.grievance.data.entity.GrievanceTransactionEntity;
import com.assist.grievance.data.mapper.GrievanceTransactionMapper;
import com.assist.grievance.data.model.request.GrievanceTransactionDto;
import com.assist.grievance.data.repository.GrievanceTransactionRepository;
import com.assist.grievance.service.GrievanceTransactionService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;


@Service
@AllArgsConstructor
@Slf4j
public class GrievanceTransactionServiceImpl implements GrievanceTransactionService {

    private final GrievanceTransactionRepository repository;
    private final GrievanceTransactionMapper mapper;

    @Override
    public ResponseEntity<ResponseData> saveGrievanceTransaction(GrievanceTransactionDto request) {
        log.info("GrievanceTransactionServiceImpl saveGrievanceTransaction request: {}", request);
        try {
            if (Objects.nonNull(request)) {
                Optional<GrievanceTransactionEntity> optExistingGrievance = repository.findByGrievanceNumber(request.getGrievanceNumber());
                if (optExistingGrievance.isEmpty()) {
                    // convert DTO to Entity
                    GrievanceTransactionEntity entity = mapper.mapToEntity(request);
                    // set extra fields
                    entity.setCreatedBy(SecurityUtils.getAuthenticatedUsername());
                    // save to DB
                    GrievanceTransactionEntity savedEntity = repository.save(entity);
                    // convert back to DTO
                    GrievanceTransactionDto responseDto = mapper.mapToDto(savedEntity);
                    // prepare response
                    return CommonUtil.generatedCreatedResponse(Map.of("grievanceTxnNo", savedEntity.getGrievanceTxnNo()));
                } else {
                    return CommonUtil.generateCustomResponse(Constants.ALREADY_AVAILABLE, Constants.ALREADY_AVAILABLE, HttpStatus.BAD_REQUEST);
                }
            } else {
                return CommonUtil.generateCustomResponse(Constants.DATA_FOUND_MESSAGE, Constants.DATA_NOT_FOUND_MESSAGE, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            log.error("GrievanceTransactionMapper saveGrievanceTransaction", e);
            return CommonUtil.generateExceptionResponse(e);

        }
    }
}
