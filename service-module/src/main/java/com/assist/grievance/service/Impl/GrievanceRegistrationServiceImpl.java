package com.assist.grievance.service.Impl;


import com.assist.grievance.Util.GrievanceNumberGenerator;
import com.assist.grievance.Util.SecurityUtils;
import com.assist.grievance.commonUtil.CommonUtil;
import com.assist.grievance.commonUtil.Constants;
import com.assist.grievance.commonUtil.ResponseData;
import com.assist.grievance.commonUtil.SearchResponseData;
import com.assist.grievance.data.entity.GrievanceEntity;
import com.assist.grievance.data.entity.GrievanceRegistrationEntity;
import com.assist.grievance.data.entity.GrievanceTransactionEntity;
import com.assist.grievance.data.enums.GrievanceStatus;
import com.assist.grievance.data.event.GrievanceCreatedEvent;
import com.assist.grievance.data.mapper.GrievanceRegistrationMapper;
import com.assist.grievance.data.mapper.GrievanceTransactionMapper;
import com.assist.grievance.data.model.request.GrievanceRegistrationDTO;
import com.assist.grievance.data.model.request.GrievanceTransactionDto;
import com.assist.grievance.data.model.request.RegisterGrievancePaginatedRequest;
import com.assist.grievance.data.model.request.RegisterTxnGrievancePageRequest;
import com.assist.grievance.data.repository.GrievanceRegistrationRepository;
import com.assist.grievance.data.repository.GrievanceRepository;
import com.assist.grievance.data.repository.GrievanceTransactionRepository;
import com.assist.grievance.data.specification.GrievanceRegisterSpecification;
import com.assist.grievance.data.specification.GrievanceTransactionSpecification;
import com.assist.grievance.producer.GrievanceKafkaProducer;
import com.assist.grievance.service.GrievanceRegistrationLogService;
import com.assist.grievance.service.GrievanceRegistrationService;
import com.assist.grievance.validator.GrievanceRegistrationValidator;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class GrievanceRegistrationServiceImpl implements GrievanceRegistrationService {

    private final GrievanceRepository grievanceRepository;
    private final GrievanceRegistrationRepository repository;
    private final GrievanceTransactionRepository grievanceTransactionRepository;
    private final GrievanceRegisterSpecification specification;
    private final GrievanceTransactionSpecification transactionSpecification;
    private final GrievanceRegistrationLogService grievanceRegistrationLogService;
    private final GrievanceNumberGenerator grievanceNumberGenerator;
    private final GrievanceRegistrationValidator validator;
    private final GrievanceRegistrationMapper mapper;
    private final GrievanceTransactionMapper transactionMapper;
    private final GrievanceKafkaProducer grievanceKafkaProducer;


    /*
     * This method is used for both auto and manual grievance registration.
     * The core processing logic remains the same for both cases.
     * The only difference lies in the request payload.
     * Manual grievance registration contains additional fields,
     * which have been handled and defined in the corresponding DTO.
     */
    @Override
    @Transactional
    public ResponseEntity<ResponseData> saveNewGrievance(GrievanceRegistrationDTO dto) {
        log.info("GrievanceRegistrationServiceImpl.saveNewGrievance");
        // TODO: The complete business requirement is not finalized yet; this API currently performs a dummy save operation.
        try {
            if (Objects.nonNull(dto)) {
                // Validate the incoming DTO
                List<String> validationErrors = validator.validate(dto);
                if (!validationErrors.isEmpty()) {
                    return CommonUtil.generateCustomResponse(
                            String.join(", ", validationErrors),
                            Constants.VALIDATION_ERROR,
                            HttpStatus.BAD_REQUEST
                    );
                }
                Optional<GrievanceEntity> optExistingGrievanceEntity = grievanceRepository.findByAcknowledgementNoAndStatusIsNotNullAndStatus(
                        dto.getAcknowledgementNo(), GrievanceStatus.RECEIVED.getStatus());
                if (optExistingGrievanceEntity.isEmpty()) {
                    return CommonUtil.generateCustomResponse(
                            Constants.DATA_NOT_FOUND_MESSAGE +
                                    " for the provided Acknowledgement Number " + dto.getAcknowledgementNo(),
                            Constants.DATA_NOT_FOUND_MESSAGE,
                            HttpStatus.BAD_REQUEST
                    );
                }
                Optional<GrievanceRegistrationEntity> optExistingEntity = repository.findByGrievanceNumberAndStatusNotNullAndStatus(
                        dto.getGrievanceNumber(), GrievanceStatus.REJISTERED.getStatus());
                /* First Time for new Registration */
                if (optExistingEntity.isEmpty()) {
                    // Convert DTO to Entity
                    GrievanceRegistrationEntity entity = mapper.mapToEntity(dto);
                    entity.setCreatedBy(SecurityUtils.getAuthenticatedUsername());
                    // Generate unique grievance number
                    String grievanceNo = grievanceNumberGenerator.generateGrievanceNumber(dto.getInsuranceCompanyName());
                    // Generate grievance txn number
                    String grievanceTxnNo = grievanceNumberGenerator.generateGrievanceTransactionNumber(grievanceNo);
                    entity.setGrievanceNumber(grievanceNo);
                    entity.setGrievanceTxnNo(grievanceTxnNo);
                    entity.setCreatedDate(LocalDateTime.now());
                    // Save in DB
                    GrievanceRegistrationEntity savedEntity = repository.save(entity);
                    /* Also Save in Grievance Transaction Table */
                    saveGrievanceTransaction(savedEntity);
                    // Maintain Audit Log
                    grievanceRegistrationLogService.saveGrievanceRegistrationLog(mapper.mapEntityToLogDto(savedEntity));
                    // Update an existing grievance identified by the acknowledgement number.
                    updateExistingGrievanceByAckNo(optExistingGrievanceEntity.get(),savedEntity);
                    // Send Mail
                    grievanceKafkaProducer.publishGrievanceCreated(
                            new GrievanceCreatedEvent(
                                    savedEntity.getGrievanceNumber(),
                                    savedEntity.getGrievanceTxnNo(),
                                    savedEntity.getSenderEmail(),
                                    savedEntity.getInsuredName(),
                                    LocalDateTime.now().toString()
                            )
                    );
                    // Convert Entity to DTO
                    GrievanceRegistrationDTO responseDto = mapper.mapToDto(savedEntity);
                    // Prepare Response
                    return CommonUtil.generatedCreatedResponse(Map.of("grienvanceNo.", savedEntity.getGrievanceNumber(),
                            "grievanceTxnNo.", savedEntity.getGrievanceTxnNo()));
                }
                /* If grievance number already exists,then this time it will not create grievance number
                only create grievance transaction number and save in grievance transaction table
                */
                else {
                    // Convert DTO to Entity
                    GrievanceRegistrationEntity entity = mapper.mapToEntity(dto);
                    entity.setCreatedBy(SecurityUtils.getAuthenticatedUsername());
                    // Generate grievance txn number
                    String grievanceTxnNo = grievanceNumberGenerator.generateGrievanceTransactionNumber(dto.getGrievanceNumber());
                    entity.setGrievanceNumber(dto.getGrievanceNumber());
                    entity.setGrievanceTxnNo(grievanceTxnNo);
                    entity.setCreatedDate(LocalDateTime.now());
                    /* Save in Grievance Transaction Table */
                    saveGrievanceTransaction(entity);
                    // Maintain Audit Log
                    grievanceRegistrationLogService.saveGrievanceRegistrationLog(mapper.mapEntityToLogDto(entity));
                    // Update an existing grievance identified by the acknowledgement number.
                    updateExistingGrievanceByAckNo(optExistingGrievanceEntity.get(),entity);

                    return CommonUtil.generatedCreatedResponse(Map.of("grienvanceNo.", entity.getGrievanceNumber(),
                            "grievanceTxnNo.", entity.getGrievanceTxnNo()));
                }
            } else {
                return CommonUtil.generateCustomResponse(Constants.DATA_FOUND_MESSAGE, Constants.DATA_NOT_FOUND_MESSAGE, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            log.error("Exception in GrievanceRegistrationServiceImpl.saveNewGrievance: ", e.getMessage());
            return CommonUtil.generateExceptionResponse(e);
        }
    }

    @Override
    public ResponseEntity<ResponseData> updateRegisterGrievance(GrievanceRegistrationDTO dto) {
        log.info("GrievanceRegistrationServiceImpl.updateRegisterGrievance");
        try {
            if (Objects.nonNull(dto)) {
                //TODO : It is just a placeholder code
                Optional<GrievanceRegistrationEntity> optExistingEntity = repository.findByGrievanceNumberAndStatusNotNullAndStatus(
                        dto.getGrievanceNumber(), GrievanceStatus.REJISTERED.getStatus());
                if (optExistingEntity.isPresent()) {
                    GrievanceRegistrationEntity existingEntity = optExistingEntity.get();
                    // Update fields
                    existingEntity.setReceivedFrom(dto.getReceivedFrom());
                    existingEntity.setOthersName(dto.getOthersName());
                    existingEntity.setInsuranceCompanyName(dto.getInsuranceCompanyName());
                    existingEntity.setGrievanceType(dto.getGrievanceType());
                    existingEntity.setGrievanceSubType(dto.getGrievanceSubType());
                    existingEntity.setGrievanceDetailType(dto.getGrievanceDetailType());
                    existingEntity.setPolicyType(dto.getPolicyType());
                    existingEntity.setPolicyNumber(dto.getPolicyNumber());
                    existingEntity.setClaimNumber(dto.getClaimNumber());
                    existingEntity.setInsuredName(dto.getInsuredName());
                    existingEntity.setPhoneNumber(dto.getPhoneNumber());
                    existingEntity.setSenderEmail(dto.getSenderEmail());
                    existingEntity.setSenderAddress(dto.getSenderAddress());
                    existingEntity.setDocumentName(dto.getDocumentName());
                    existingEntity.setDocumentPath(dto.getDocumentPath());
                    existingEntity.setRemarks(dto.getRemarks());
                    existingEntity.setEmailTo(dto.getEmailTo());
                    existingEntity.setEmailCc(dto.getEmailCc());
                    existingEntity.setEmailBcc(dto.getEmailBcc());
                    existingEntity.setUpdatedAt(LocalDateTime.now());
                    existingEntity.setUpdatedBy(SecurityUtils.getAuthenticatedUsername());
                    // Save updated entity
                    repository.save(existingEntity);
                    // Prepare Response
                    return CommonUtil.generateOkResponse("Grievance updated successfully.");
                } else {
                    return CommonUtil.generateCustomResponse(Constants.DATA_FOUND_MESSAGE, Constants.DATA_NOT_FOUND_MESSAGE, HttpStatus.BAD_REQUEST);
                }

            } else {
                return CommonUtil.generateCustomResponse(Constants.DATA_FOUND_MESSAGE, Constants.DATA_NOT_FOUND_MESSAGE, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            log.error("Exception in GrievanceRegistrationServiceImpl.updateRegisterGrievance: ", e.getMessage());
            return CommonUtil.generateExceptionResponse(e);
        }
    }

    @Override
    public ResponseEntity<ResponseData> getRegisterGrievance(String grievanceNumber) {
        log.info("Entering getRegisterGrievance with grievanceNumber: {}", grievanceNumber);

        try {
            if (Objects.isNull(grievanceNumber)) {
                return CommonUtil.generateCustomResponse(
                        Constants.DATA_NOT_FOUND_MESSAGE,
                        Constants.DATA_NOT_FOUND_MESSAGE,
                        HttpStatus.BAD_REQUEST
                );
            }

            return repository.findByGrievanceNumberAndStatusNotNullAndStatus(
                            grievanceNumber, GrievanceStatus.REJISTERED.getStatus())
                    .map(entity -> {
                        GrievanceRegistrationDTO responseDto = mapper.mapToDto(entity);
                        return CommonUtil.generateOkResponse(responseDto);
                    })
                    .orElseGet(() -> CommonUtil.generateCustomResponse(
                            Constants.DATA_NOT_FOUND_MESSAGE,
                            Constants.DATA_NOT_FOUND_MESSAGE,
                            HttpStatus.BAD_REQUEST
                    ));

        } catch (Exception e) {
            log.error("Exception occurred in getRegisterGrievance", e);
            return CommonUtil.generateExceptionResponse(e);
        }
    }


    @Override
    public ResponseEntity<SearchResponseData<GrievanceRegistrationDTO>> findPaginatedRegisterGrievance(RegisterGrievancePaginatedRequest request) {
        log.info("GrievanceRegistrationServiceImpl.findPaginatedRegisterGrievance");
        try {

            // Pagination
            int page = Math.max(request.getPage(), 0);
            int size = request.getSize() <= 0 ? 10 : request.getSize();

            // Sorting
            Sort.Direction direction = "asc".equalsIgnoreCase(request.getSortDirection())
                    ? Sort.Direction.ASC
                    : Sort.Direction.DESC;

            Sort sort = Sort.by(direction,
                    request.getSort().toArray(new String[0]));

            Pageable pageable = PageRequest.of(page, size, sort);

            // Specification build
            Specification<GrievanceRegistrationEntity> spec = specification.build(request);

            // DB call
            Page<GrievanceRegistrationEntity> paginatedData = repository.findAll(spec, pageable);

            // Empty page handling
            if (paginatedData.isEmpty()) {
                return CommonUtil.generateSearchOkResponse(Page.empty(pageable));
            }

            // Entity to DTO mapping
            Page<GrievanceRegistrationDTO> dtoPage = paginatedData.map(mapper::mapToDto);

            // Final response
            return CommonUtil.generateSearchOkResponse(dtoPage);


        } catch (Exception e) {
            log.error("Exception in GrievanceRegistrationServiceImpl.findPaginatedRegisterGrievance: ", e.getMessage());
            return CommonUtil.generateSearchExceptionResponse(e);
        }
    }

    /**
     * @param grievanceNumber
     * @return
     */
    @Override
    public ResponseEntity<ResponseData> getRegisterGrievanceTxnByGrievanceNo(String grievanceNumber) {
        log.info("Entering getRegisterGrievanceTxnByGrievanceNo with grievanceNumber: {}", grievanceNumber);
        try {
            // Null/Empty check
            if (Objects.isNull(grievanceNumber) || grievanceNumber.trim().isEmpty()) {
                return CommonUtil.generateCustomResponse(
                        Constants.DATA_NOT_FOUND_MESSAGE,
                        Constants.DATA_NOT_FOUND_MESSAGE,
                        HttpStatus.BAD_REQUEST
                );
            }

            // Fetch transactions from DB
            List<GrievanceTransactionEntity> entities =
                    grievanceTransactionRepository.findAllByGrievanceNumber(grievanceNumber);

            // Handle case when no transactions are found for the given grievance number
            if (entities.isEmpty()) {
                return CommonUtil.generateCustomResponse(
                        Constants.DATA_NOT_FOUND_MESSAGE,
                        Constants.DATA_NOT_FOUND_MESSAGE,
                        HttpStatus.NOT_FOUND
                );
            }

            // Convert entities to DTOs
            List<GrievanceTransactionDto> dtoList = entities.stream()
                    .map(transactionMapper::mapToDto)
                    .toList();

            return CommonUtil.generateOkResponse(dtoList);

        } catch (Exception e) {
            log.error("Exception occurred in getRegisterGrievanceTxnByGrievanceNo", e);
            return CommonUtil.generateExceptionResponse(e);
        }
    }

    /**
     * @param request
     * @return
     */
    @Override
    public ResponseEntity<SearchResponseData<GrievanceTransactionDto>> findPaginatedRegisterTxnGrievances(RegisterTxnGrievancePageRequest request) {
        log.info("GrievanceRegistrationServiceImpl.findPaginatedRegisterTxnGrievances");
        try {

            // Pagination
            int page = Math.max(request.getPage(), 0);
            int size = request.getSize() <= 0 ? 10 : request.getSize();

            // Sorting
            Sort.Direction direction = "asc".equalsIgnoreCase(request.getSortDirection())
                    ? Sort.Direction.ASC
                    : Sort.Direction.DESC;

            Sort sort = Sort.by(direction,
                    request.getSort().toArray(new String[0]));

            Pageable pageable = PageRequest.of(page, size, sort);

            // Specification build
            Specification<GrievanceTransactionEntity> spec = transactionSpecification.build(request);
            // DB call
            Page<GrievanceTransactionEntity> paginatedData = grievanceTransactionRepository.findAll(spec, pageable);
            // Empty page handling
            if (paginatedData.isEmpty()) {
                return CommonUtil.generateSearchOkResponse(Page.empty(pageable));
            }
            // Entity to DTO mapping
            Page<GrievanceTransactionDto> dtoPage = paginatedData.map(transactionMapper::mapToDto);
            // Final response
            return CommonUtil.generateSearchOkResponse(dtoPage);
        } catch (Exception e) {
            log.error("Exception in GrievanceRegistrationServiceImpl.findPaginatedRegisterTxnGrievances: ", e.getMessage());
            return CommonUtil.generateSearchExceptionResponse(e);
        }
    }

    // Update existing grievance identified by the acknowledgement number
    private void updateExistingGrievanceByAckNo(GrievanceEntity existingGrievanceEntity, GrievanceRegistrationEntity registrationEntity) {
        existingGrievanceEntity.setStatus(GrievanceStatus.REJISTERED.getStatus());
        existingGrievanceEntity.setUpdatedAt(LocalDateTime.now());
        existingGrievanceEntity.setUpdatedBy(SecurityUtils.getAuthenticatedUsername());
        existingGrievanceEntity.setGrievanceNo(registrationEntity.getGrievanceNumber());
        grievanceRepository.save(existingGrievanceEntity);
    }

    private void saveGrievanceTransaction(GrievanceRegistrationEntity savedEntity) {
        GrievanceTransactionEntity txnEntity = GrievanceTransactionEntity.builder()
                .acknowledgementNo(savedEntity.getAcknowledgementNo())
                .grievanceNumber(savedEntity.getGrievanceNumber())
                .grievanceTxnNo(savedEntity.getGrievanceTxnNo())
                .status(savedEntity.getStatus())
                .grievanceType(savedEntity.getGrievanceType())
                .claimNo(savedEntity.getClaimNumber())
                .mode(savedEntity.getMode())
                .insuranceCompany(savedEntity.getInsuranceCompanyName())
                .createdBy(SecurityUtils.getAuthenticatedUsername())
                .updatedBy(SecurityUtils.getAuthenticatedUsername())
                .build();
        grievanceTransactionRepository.save(txnEntity);

    }
}
