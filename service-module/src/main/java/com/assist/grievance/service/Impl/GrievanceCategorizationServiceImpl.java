package com.assist.grievance.service.Impl;

import com.assist.grievance.Util.SecurityUtils;
import com.assist.grievance.commonUtil.CommonUtil;
import com.assist.grievance.commonUtil.Constants;
import com.assist.grievance.commonUtil.ResponseData;
import com.assist.grievance.commonUtil.SearchResponseData;
import com.assist.grievance.data.entity.DocumentEntity;
import com.assist.grievance.data.entity.GrievanceCategorizationEntity;
import com.assist.grievance.data.entity.GrievanceRegistrationEntity;
import com.assist.grievance.data.mapper.GrievanceCategorizationMapper;
import com.assist.grievance.data.model.request.DocumentDto;
import com.assist.grievance.data.model.request.GrievanceCategorizationDto;
import com.assist.grievance.data.model.request.GrievanceCategorizationPaginatedRequest;
import com.assist.grievance.data.repository.GrievanceCategorizationRepository;
import com.assist.grievance.data.repository.GrievanceRegistrationRepository;
import com.assist.grievance.data.specification.GrievanceCategorizationSpecification;
import com.assist.grievance.service.GrievanceCategorizationLogService;
import com.assist.grievance.service.GrievanceCategorizationService;
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
import java.util.*;

@Service
@AllArgsConstructor
@Slf4j
public class GrievanceCategorizationServiceImpl implements GrievanceCategorizationService {

    private final GrievanceCategorizationRepository grievanceCategorizationRepository;
    private final GrievanceCategorizationLogService grievanceCategorizationLogService;
    private final GrievanceRegistrationRepository grievanceRegistrationRepositoryRepository;
    private final GrievanceCategorizationSpecification specification;
    private final GrievanceCategorizationMapper mapper;


    @Override
    @Transactional
    public ResponseEntity<ResponseData> saveGrievanceCategorization(GrievanceCategorizationDto dto) {
        log.info("GrievanceCategorizationServiceImpl saveGrievanceCategorization");
        try {
            if (Objects.nonNull(dto)) {
                Optional<GrievanceRegistrationEntity> optExistingRegisterGrievance = grievanceRegistrationRepositoryRepository.findByGrievanceNumberAndStatusNotNullAndStatus(dto.getGrievanceNo(), "registered");
                if (optExistingRegisterGrievance.isEmpty()) {
                    return CommonUtil.generateCustomResponse(
                            Constants.DATA_NOT_FOUND_MESSAGE +
                                    " for the provided Grievance Number " + dto.getGrievanceNo(),
                            Constants.DATA_NOT_FOUND_MESSAGE,
                            HttpStatus.BAD_REQUEST
                    );
                }
                Optional<GrievanceCategorizationEntity> optExistingEntity = grievanceCategorizationRepository.findByGrievanceNumber(dto.getGrievanceNo());
                if (optExistingEntity.isEmpty()) {
                    // Convert DTO to Entity
                    GrievanceCategorizationEntity entity = mapper.dtoToEntity(dto);
                    entity.setCreatedBy(SecurityUtils.getAuthenticatedUsername());
                    entity.setCreatedAt(LocalDateTime.now());
                    // Documents handling can be added here if needed
                    handlingDocuments(dto, entity);
                    // Save in DB
                    grievanceCategorizationRepository.save(entity);
                    // Log the saved entity
                    grievanceCategorizationLogService.saveGrievanceCategorizationLog(mapper.entityToLogDto(entity));
                    // Update existing grievance registration status
                    updateExistingRegisterGrievance(optExistingRegisterGrievance.get());
                    // Convert Entity to DTO
                    GrievanceCategorizationDto responseDto = mapper.entityToDto(entity);
                    // Prepare Response
                    return CommonUtil.generatedCreatedResponse(Map.of("GrivanceNumber.", entity.getGrievanceNumber()));
                } else {
                    return CommonUtil.generateCustomResponse(Constants.ALREADY_AVAILABLE, Constants.ALREADY_AVAILABLE, HttpStatus.BAD_REQUEST);
                }
            } else {
                return CommonUtil.generateCustomResponse(Constants.DATA_FOUND_MESSAGE, Constants.DATA_NOT_FOUND_MESSAGE, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            log.error("Error in saveGrievanceCategorization: {}", e.getMessage());
            return CommonUtil.generateExceptionResponse(e);
        }
    }

    @Override
    public ResponseEntity<SearchResponseData<GrievanceCategorizationDto>> getPaginatedCategorizationData(GrievanceCategorizationPaginatedRequest request) {
        log.info("GrievanceCategorizationServiceImpl getPaginatedCategorizationData");
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
            Specification<GrievanceCategorizationEntity> spec = specification.build(request);

            // DB Call
            Page<GrievanceCategorizationEntity> paginatedData = grievanceCategorizationRepository.findAll(spec, pageable);

            // Empty page handling
            if (paginatedData.isEmpty()) {
                return CommonUtil.generateSearchOkResponse(Page.empty(pageable));
            }

            // Entity to DTO mapping
            Page<GrievanceCategorizationDto> dtoPage = paginatedData.map(mapper::entityToDto);

            // Final response
            return CommonUtil.generateSearchOkResponse(dtoPage);


        } catch (Exception e) {
            log.error("Error in getPaginatedCategorizationData: {}", e.getMessage());
            return CommonUtil.generateSearchExceptionResponse(e);
        }
    }

    @Override
    public ResponseEntity<ResponseData> getCategorizationByGrievanceNo(String grievanceNo) {
        log.info("GrievanceCategorizationServiceImpl getCategorizationByGrievanceNo");
        try {
            if (Objects.isNull(grievanceNo)) {
                return CommonUtil.generateCustomResponse(
                        Constants.DATA_NOT_FOUND_MESSAGE,
                        Constants.DATA_NOT_FOUND_MESSAGE,
                        HttpStatus.BAD_REQUEST
                );
            }
            return grievanceCategorizationRepository.findByGrievanceNumber(grievanceNo)
                    .map(entity -> {
                        GrievanceCategorizationDto responseDto = mapper.entityToDto(entity);
                        return CommonUtil.generateOkResponse(responseDto);
                    })
                    .orElseGet(() -> CommonUtil.generateCustomResponse(
                            Constants.DATA_NOT_FOUND_MESSAGE,
                            Constants.DATA_NOT_FOUND_MESSAGE,
                            HttpStatus.BAD_REQUEST
                    ));
        } catch (Exception e) {
            log.error("Error in getCategorizationByGrievanceNo: {}", e.getMessage());
            return CommonUtil.generateExceptionResponse(e);
        }
    }

    // Helper method to update existing grievance registration
    private void updateExistingRegisterGrievance(GrievanceRegistrationEntity existingGrievance) {
        existingGrievance.setStatus("processed");
        existingGrievance.setUpdatedAt(LocalDateTime.now());
        existingGrievance.setUpdatedBy(SecurityUtils.getAuthenticatedUsername());
        grievanceRegistrationRepositoryRepository.save(existingGrievance);
    }


    private void handlingDocuments(GrievanceCategorizationDto dto, GrievanceCategorizationEntity entity) {
        List<DocumentEntity> documents = new ArrayList<>();

        // Map each DTO list to DocumentEntity
        if (dto.getCopyOfFir() != null) {
            dto.getCopyOfFir().forEach(d -> {
                DocumentEntity doc = createDocumentEntity(d, entity, "COPY_OF_FIR");
                documents.add(doc);
            });
        }

        if (dto.getValidIdProof() != null) {
            dto.getValidIdProof().forEach(d -> {
                DocumentEntity doc = createDocumentEntity(d, entity, "VALID_ID_PROOF");
                documents.add(doc);
            });
        }

        if (dto.getValidAddressProof() != null) {
            dto.getValidAddressProof().forEach(d -> {
                DocumentEntity doc = createDocumentEntity(d, entity, "VALID_ADDRESS_PROOF");
                documents.add(doc);
            });
        }

        if (dto.getReplyFromInsuranceCompany() != null) {
            dto.getReplyFromInsuranceCompany().forEach(d -> {
                DocumentEntity doc = createDocumentEntity(d, entity, "REPLY_FROM_INSURANCE_COMPANY");
                documents.add(doc);
            });
        }

        if (dto.getComplaintToInsuranceOmbudsman() != null) {
            dto.getComplaintToInsuranceOmbudsman().forEach(d -> {
                DocumentEntity doc = createDocumentEntity(d, entity, "COMPLAINT_TO_INSURANCE_OMBUDSMAN");
                documents.add(doc);
            });
        }

        if (dto.getReplyFromInsuranceOmbudsman() != null) {
            dto.getReplyFromInsuranceOmbudsman().forEach(d -> {
                DocumentEntity doc = createDocumentEntity(d, entity, "REPLY_FROM_INSURANCE_OMBUDSMAN");
                documents.add(doc);
            });
        }

        if (dto.getCopyOfInsurancePolicy() != null) {
            dto.getCopyOfInsurancePolicy().forEach(d -> {
                DocumentEntity doc = createDocumentEntity(d, entity, "COPY_OF_INSURANCE_POLICY");
                documents.add(doc);
            });
        }

        if (dto.getCopyOfComplaintToInsuranceCompany() != null) {
            dto.getCopyOfComplaintToInsuranceCompany().forEach(d -> {
                DocumentEntity doc = createDocumentEntity(d, entity, "COPY_OF_COMPLAINT_TO_INSURANCE_COMPANY");
                documents.add(doc);
            });
        }

        // Set to entity
        entity.setDocument(documents);
    }

    private DocumentEntity createDocumentEntity(DocumentDto dto, GrievanceCategorizationEntity entity, String type) {
        return DocumentEntity.builder()
                .name(dto.getName())
                .data(dto.getData())
                .documentType(type)
                .grievanceCase(entity)
                .grievanceNumber(entity.getGrievanceNumber())
                .uploadedAt(LocalDateTime.now())
                .build();
    }

}
