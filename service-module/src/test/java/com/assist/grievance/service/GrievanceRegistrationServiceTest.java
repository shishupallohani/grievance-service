package com.assist.grievance.service;

import com.assist.grievance.Util.GrievanceNumberGenerator;
import com.assist.grievance.Util.SecurityUtils;
import com.assist.grievance.commonUtil.Constants;
import com.assist.grievance.commonUtil.ResponseData;
import com.assist.grievance.data.entity.GrievanceEntity;
import com.assist.grievance.data.entity.GrievanceRegistrationEntity;
import com.assist.grievance.data.mapper.GrievanceRegistrationMapper;
import com.assist.grievance.data.model.request.GrievanceRegistrationDTO;
import com.assist.grievance.data.model.request.GrievanceRegistrationLogDto;
import com.assist.grievance.data.repository.GrievanceRegistrationRepository;
import com.assist.grievance.data.repository.GrievanceRepository;
import com.assist.grievance.data.specification.GrievanceRegisterSpecification;
import com.assist.grievance.service.Impl.GrievanceRegistrationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GrievanceRegistrationServiceTest {

    @Mock
    private GrievanceRegistrationRepository repository;

    @Mock
    private GrievanceRepository grievanceRepository;

    @Mock
    private GrievanceRegisterSpecification specification;

    @Mock
    private GrievanceRegistrationLogService grievanceRegistrationLogService;

    @Mock
    private GrievanceNumberGenerator grievanceNumberGenerator;

    @Mock
    private GrievanceRegistrationMapper mapper;

    @InjectMocks
    private GrievanceRegistrationServiceImpl service;

    @Captor
    private ArgumentCaptor<GrievanceRegistrationEntity> entityCaptor;

    @Captor
    private ArgumentCaptor<GrievanceEntity> grievanceEntityCaptor;

    private GrievanceEntity mockGrievanceEntity;
    private GrievanceRegistrationEntity mockEntity;
    private GrievanceRegistrationDTO mockRequest;
    private GrievanceRegistrationLogDto mockLogDto;

    private static final String TEST_ACK_NO = "ACK1234567890";
    private static final String TEST_GRIEVANCE_NO = "GRNIA202500001";
    private static final String TEST_USERNAME = "test.user@example.com";
    private static final String INSURANCE_COMPANY = "NIA";

    @BeforeEach
    void setUp() {
        // Set up valid GrievanceEntity
        mockGrievanceEntity = GrievanceEntity.builder()
                .id(1)
                .acknowledgementNo(TEST_ACK_NO)
                .status("received")
                .insuranceCompany(INSURANCE_COMPANY)
                .build();

        // Set up valid mockRequest object
        mockRequest = GrievanceRegistrationDTO.builder()
                .acknowledgementNo(TEST_ACK_NO)
                .grievanceNumber(TEST_GRIEVANCE_NO)
                .insuranceCompanyName(INSURANCE_COMPANY)
                .department("Customer Service")
                .senderEmail("test@gmail.com")
                .receivedFrom("Customer")
                .grievanceType("Service Delay")
                .grievanceSubType("Delay in Response")
                .build();

        // Set up valid mockEntity object
        mockEntity = GrievanceRegistrationEntity.builder()
                .id(1)
                .grievanceNumber(TEST_GRIEVANCE_NO)
                .department("Customer Service")
                .senderEmail("test@gmail.com")
                .receivedFrom("Customer")
                .grievanceType("Service Delay")
                .grievanceSubType("Delay in Response")
                .createdBy(TEST_USERNAME)
                .createdDate(LocalDateTime.now())
                .build();

        // Set up log DTO
        mockLogDto = GrievanceRegistrationLogDto.builder()
                .grievanceNumber(TEST_GRIEVANCE_NO)
                .build();
    }

    /* Test case for successful grievance registration when no existing grievance is found */
    @Test
    @DisplayName("Register Grievance - Valid Request, No Existing Grievance")
    @Disabled
    void registerGrievance_WhenValidRequestAndNoExistingGrievance_ShouldGenerateGrievanceSuccessfully() {

        // GIVEN - Setup test data and mock behaviors
        // Mock repository to return existing grievance with acknowledgement number
        when(grievanceRepository
                .findByAcknowledgementNoAndStatusIsNotNullAndStatus(TEST_ACK_NO, "received"))
                .thenReturn(Optional.of(mockGrievanceEntity));

        // Mock repository to return empty (no existing grievance with same grievance number)
        when(repository.findByGrievanceNumberAndStatusNotNullAndStatus(TEST_GRIEVANCE_NO, "registered"))
                .thenReturn(Optional.empty());

        // Mock mapper to convert request DTO to entity
        when(mapper.mapToEntity(mockRequest)).thenReturn(mockEntity);

        // Mock mapper to convert Entity back to DTO
        when(mapper.mapToDto(any(GrievanceRegistrationEntity.class))).thenReturn(mockRequest);

        // Mock mapper to convert entity to log DTO
        when(mapper.mapEntityToLogDto(any(GrievanceRegistrationEntity.class))).thenReturn(mockLogDto);

        // Mock SecurityUtils static method
        try (MockedStatic<SecurityUtils> securityUtilsMock = mockStatic(SecurityUtils.class)) {
            securityUtilsMock.when(SecurityUtils::getAuthenticatedUsername)
                    .thenReturn(TEST_USERNAME);

            // Mock grievance number generation
            when(grievanceNumberGenerator.generateGrievanceNumber(INSURANCE_COMPANY))
                    .thenReturn(TEST_GRIEVANCE_NO);

            // Mock repository save operation for GrievanceRegistrationEntity
            when(repository.save(any(GrievanceRegistrationEntity.class))).thenReturn(mockEntity);

            // Mock repository save operation for GrievanceEntity (update operation)
            when(grievanceRepository.save(any(GrievanceEntity.class))).thenReturn(mockGrievanceEntity);

            // Mock log service to return saved log DTO
            when(grievanceRegistrationLogService.saveGrievanceRegistrationLog(any(GrievanceRegistrationLogDto.class)))
                    .thenReturn(mockLogDto);

            // WHEN - Execute the method under test
            ResponseEntity<ResponseData> actualResponse = service.saveNewGrievance(mockRequest);


            // THEN - Verify the results and interactions
            // 1. Verify response is not null
            assertThat(actualResponse).isNotNull();

            // 2. Verify HTTP status code
            assertThat(actualResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

            // 3. Verify response body
            ResponseData responseBody = actualResponse.getBody();
            assertThat(responseBody).isNotNull();
            assertThat(responseBody.getStatus()).isEqualTo(Constants.SUCCESS);
            assertThat(responseBody.getMessage()).isEqualTo(Constants.SUCCESS_MESSAGE);

            // 4. Verify response data contains grievance number
            assertThat(responseBody.getData()).isInstanceOf(Map.class);
            Map<String, String> dataMap = (Map<String, String>) responseBody.getData();
            assertNotNull(dataMap, "Data map should not be null");
            assertTrue(dataMap.containsKey("grienvanceNo."), "Data map should contain grienvanceNo. key");
            assertThat(dataMap.get("grienvanceNo.")).isEqualTo(TEST_GRIEVANCE_NO);

            // 5. Verify grievanceRepository interactions
            verify(grievanceRepository, times(1))
                    .findByAcknowledgementNoAndStatusIsNotNullAndStatus(TEST_ACK_NO, "received");

            // 6. Verify registration repository interactions
            verify(repository, times(1))
                    .findByGrievanceNumberAndStatusNotNullAndStatus(TEST_GRIEVANCE_NO, "registered");

            // 7. Verify save was called and capture the registration entity
            verify(repository, times(1)).save(entityCaptor.capture());
            GrievanceRegistrationEntity savedEntity = entityCaptor.getValue();

            // 8. Verify saved registration entity properties
            assertThat(savedEntity.getGrievanceNumber()).isEqualTo(TEST_GRIEVANCE_NO);
            assertThat(savedEntity.getCreatedBy()).isEqualTo(TEST_USERNAME);
            assertThat(savedEntity.getCreatedDate()).isNotNull();
            assertThat(savedEntity.getDepartment()).isEqualTo("Customer Service");

            // 9. Verify grievance number generator was called
            verify(grievanceNumberGenerator, times(1)).generateGrievanceNumber(INSURANCE_COMPANY);

            // 10. Verify mapper interactions
            verify(mapper, times(1)).mapToEntity(mockRequest);
            verify(mapper, times(1)).mapToDto(any(GrievanceRegistrationEntity.class));
            verify(mapper, times(1)).mapEntityToLogDto(any(GrievanceRegistrationEntity.class));

            // 11. Verify log service was called
            verify(grievanceRegistrationLogService, times(1))
                    .saveGrievanceRegistrationLog(any(GrievanceRegistrationLogDto.class));

            // 12. Verify existing grievance was updated and saved
            verify(grievanceRepository, times(1)).save(grievanceEntityCaptor.capture());
            GrievanceEntity updatedGrievanceEntity = grievanceEntityCaptor.getValue();

            // 13. Verify updated grievance entity properties
            assertThat(updatedGrievanceEntity.getStatus()).isEqualTo("processed");
            assertThat(updatedGrievanceEntity.getUpdatedAt()).isNotNull();
            assertThat(updatedGrievanceEntity.getUpdatedBy()).isEqualTo(TEST_USERNAME);
            assertThat(updatedGrievanceEntity.getAcknowledgementNo()).isEqualTo(TEST_ACK_NO);

            // 14. Verify SecurityUtils was called twice (once for registration, once for update)
            securityUtilsMock.verify(SecurityUtils::getAuthenticatedUsername, times(2));
        }
    }
}