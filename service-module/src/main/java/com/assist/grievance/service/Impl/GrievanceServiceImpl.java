package com.assist.grievance.service.Impl;

import com.assist.grievance.Util.SecurityUtils;
import com.assist.grievance.commonUtil.CommonUtil;
import com.assist.grievance.commonUtil.Constants;
import com.assist.grievance.commonUtil.ResponseData;
import com.assist.grievance.commonUtil.SearchResponseData;
import com.assist.grievance.data.entity.DocumentEntity;
import com.assist.grievance.data.entity.GrievanceEntity;
import com.assist.grievance.data.enums.GrievanceStatus;
import com.assist.grievance.data.enums.ModeConstant;
import com.assist.grievance.data.mapper.GrievanceMapper;
import com.assist.grievance.data.model.request.GrievancePaginatedRequest;
import com.assist.grievance.data.model.request.GrievanceRequestDto;
import com.assist.grievance.data.model.request.GrievanceSummaryDto;
import com.assist.grievance.data.repository.GrievanceRepository;
import com.assist.grievance.data.specification.GrievanceSpecification;
import com.assist.grievance.exception.RecordsAlreadyExistsException;
import com.assist.grievance.exception.RecordsNotFoundException;
import com.assist.grievance.exception.ValidationException;
import com.assist.grievance.service.GrievanceService;
import com.assist.grievance.validator.GrievanceValidator;
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
import java.util.*;


@Service
@AllArgsConstructor
@Slf4j
public class GrievanceServiceImpl implements GrievanceService {

    private final GrievanceRepository grievanceRepository;
    private final GrievanceSpecification specification;
    private final GrievanceValidator grievanceValidator;
    private final GrievanceMapper mapper;

    /*
     * This method saves all types of received grievances into the database.
     * It handles grievances received through both online channels
     * (email and portal) and offline channels (manual entry via the New Inward screen).
     * The grievance data is stored in the "grievance" database table
     * along with the status marked as "received".
     */
    @Override
    public ResponseEntity<ResponseData> receivedGrievance(GrievanceRequestDto request) {
        log.info("GrievanceServiceImpl receivedGrievance");

        if (Objects.isNull(request)) {
            throw new ValidationException(Constants.PAYLOAD_EMPTY, HttpStatus.BAD_REQUEST);
        }

        // Validate the request
        List<String> validationErrors = grievanceValidator.validate(request);
        if (!validationErrors.isEmpty()) {
            throw new ValidationException(String.join(", ", validationErrors), HttpStatus.BAD_REQUEST);
        }

        // Check if grievance already exists
        Optional<GrievanceEntity> optExistingGrievance = grievanceRepository
                .findByAcknowledgementNoAndStatusIsNotNullAndStatus(request.getAcknowledgementNo(), GrievanceStatus.RECEIVED.getStatus());

        if (optExistingGrievance.isPresent()) {
            throw new RecordsAlreadyExistsException(Constants.ALREADY_AVAILABLE, HttpStatus.BAD_REQUEST);
        }
        // Map DTO -> Entity
        GrievanceEntity grievanceEntity = mapper.mapToEntity(request);
        grievanceEntity.setAcknowledgementNo(generateAcknowledgementNo());
        grievanceEntity.setCreatedBy(SecurityUtils.getAuthenticatedUsername());

        // Handle documents
        handleDocuments(request, grievanceEntity);

        // Set mode and status
        grievanceEntity.setMode(ModeConstant.fromString(request.getMode()).getStatus());
        grievanceEntity.setStatus(GrievanceStatus.RECEIVED.getStatus());

        // Save to DB
        grievanceRepository.save(grievanceEntity);

        return CommonUtil.generatedCreatedResponse(Map.of("acknowledgementNo", grievanceEntity.getAcknowledgementNo()));
    }


    @Override
    public ResponseEntity<ResponseData> updateGrievance(GrievanceRequestDto request) {
        log.info("GrievanceServiceImpl UpdateGrievance");
        try {
            if (Objects.nonNull(request)) {
                Optional<GrievanceEntity> optExistingGrievance = grievanceRepository.findByAcknowledgementNoAndStatusIsNotNullAndStatus(
                        request.getAcknowledgementNo(), GrievanceStatus.PENDING.getStatus());
                if (optExistingGrievance.isPresent()) {
                    GrievanceEntity existingGrievance = optExistingGrievance.get();
                    // Update fields
                    existingGrievance.setInsuranceCompany(request.getInsuranceCompany());
                    existingGrievance.setEmailSubject(request.getEmailSubject());
                    existingGrievance.setEmailId(request.getEmailId());
                    existingGrievance.setReceivedFrom(request.getReceivedFrom());
                    existingGrievance.setGrievanceType(request.getGrievanceType());
                    existingGrievance.setStatus(request.getStatus());
                    existingGrievance.setUpdatedAt(LocalDateTime.now());
                    // Save updated entity
                    grievanceRepository.save(existingGrievance);
                    // Convert Entity to DTO
                    GrievanceRequestDto responseDto = mapper.mapToDto(existingGrievance);
                    // Prepare Response
                    return CommonUtil.generatedCreatedResponse(Map.of("acknowledgementNo", responseDto.getAcknowledgementNo()));

                } else {
                    return CommonUtil.generateCustomResponse(Constants.REGISTRATION_NUMBER_NOT_REGISTERED, Constants.REGISTRATION_NUMBER_NOT_REGISTERED, HttpStatus.BAD_REQUEST);
                }
            } else {
                return CommonUtil.generateCustomResponse(Constants.DATA_FOUND_MESSAGE, Constants.DATA_NOT_FOUND_MESSAGE, HttpStatus.BAD_REQUEST);
            }

        } catch (IllegalArgumentException e) {
            log.error("Invalid Mode received: {}", e.getMessage());
            return CommonUtil.generateCustomResponse("INVALID_MODE", e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Exception in UpdateGrievance: {}", e.getMessage());
            return CommonUtil.generateExceptionResponse(e);
        }
    }

    @Override
    public ResponseEntity<ResponseData> getGrievanceByAckNo(String ackNo) {
        log.info("GrievanceServiceImpl getGrievanceByAckNo");
        if (Objects.isNull(ackNo)) {
            throw new ValidationException(Constants.PAYLOAD_EMPTY, HttpStatus.BAD_REQUEST);
        }
        Optional<GrievanceEntity> optExistingEntity = grievanceRepository.findByAcknowledgementNoAndStatusIsNotNullAndStatus(
                ackNo, GrievanceStatus.RECEIVED.getStatus());

        if (optExistingEntity.isEmpty()) {
            throw new RecordsNotFoundException(Constants.DATA_NOT_FOUND_MESSAGE, HttpStatus.NOT_FOUND);
        }

        GrievanceRequestDto grievanceRequestDto = mapper.mapToDto(optExistingEntity.get());
        return CommonUtil.generateOkResponse(grievanceRequestDto);
    }


    @Override
    public ResponseEntity<SearchResponseData<GrievanceRequestDto>> findPaginatedGrievance(GrievancePaginatedRequest request) {
        log.info("GrievanceServiceImpl | findPaginatedGrievance");

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
        Specification<GrievanceEntity> spec = specification.build(request);

        // DB call
        Page<GrievanceEntity> grievancePage =
                grievanceRepository.findAll(spec, pageable);

        // Empty page handling
        if (grievancePage.isEmpty()) {
            return CommonUtil.generateSearchOkResponse(Page.empty(pageable));
        }

        // Entity to DTO mapping
        Page<GrievanceRequestDto> dtoPage = grievancePage.map(mapper::mapToDto);

        // Final response
        return CommonUtil.generateSearchOkResponse(dtoPage);
    }

    @Override
    public ResponseEntity<ResponseData> getGrievanceSummary() {
        log.info("GrievanceServiceImpl >> getGrievanceSummary");

        List<Object[]> results = grievanceRepository.fetchGrievanceSummary();

        if (results.isEmpty()) {
            return CommonUtil.generateCustomResponse(
                    Constants.DATA_NOT_FOUND_MESSAGE,
                    Constants.DATA_NOT_FOUND_MESSAGE,
                    HttpStatus.BAD_REQUEST
            );
        }

        List<GrievanceSummaryDto> responseList = results.stream()
                .map(obj -> new GrievanceSummaryDto(
                        (String) obj[0],              // mode
                        ((Number) obj[1]).longValue(),// total
                        ((Number) obj[2]).longValue(),// processed
                        ((Number) obj[3]).longValue() // pending
                ))
                .toList();

        return CommonUtil.generateOkResponse(responseList);

    }


    //TODO: TEMPORARY METHOD TO GENERATE ACKNOWLEDGEMENT NO
    private String generateAcknowledgementNo() {
        String prefix = "ACK";
        String timestamp = String.valueOf(System.currentTimeMillis());
        return prefix + timestamp;
    }

    private void handleDocuments(GrievanceRequestDto dto, GrievanceEntity grievanceEntity) {
        List<DocumentEntity> documents = new ArrayList<>();
        if (dto.getDocuments() != null) {
            dto.getDocuments().forEach(doc -> {
                documents.add(
                        DocumentEntity.builder()
                                .data(doc.getData())
                                .name(doc.getName())
                                .acknowledgementNumber(grievanceEntity.getAcknowledgementNo())
                                .grievance(grievanceEntity)
                                .build()
                );
            });
        }
        grievanceEntity.setDocument(documents);
    }
}
