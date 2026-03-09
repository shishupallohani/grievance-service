package com.assist.grievance.service;

import com.assist.grievance.Util.SecurityUtils;
import com.assist.grievance.commonUtil.Constants;
import com.assist.grievance.commonUtil.ResponseData;
import com.assist.grievance.data.entity.GrievanceEntity;
import com.assist.grievance.data.enums.GrievanceStatus;
import com.assist.grievance.data.enums.ModeConstant;
import com.assist.grievance.data.mapper.GrievanceMapper;
import com.assist.grievance.data.model.request.GrievanceRequestDto;
import com.assist.grievance.data.repository.GrievanceRepository;
import com.assist.grievance.data.specification.GrievanceSpecification;
import com.assist.grievance.exception.RecordsAlreadyExistsException;
import com.assist.grievance.service.Impl.GrievanceServiceImpl;
import com.assist.grievance.validator.GrievanceValidator;
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
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GrievanceServiceImplJunitTest {

    @Mock
    private GrievanceRepository grievanceRepository;

    @Mock
    private GrievanceSpecification specification;

    @Mock
    private GrievanceMapper mapper;

    @Spy
    private GrievanceValidator validator;

    @InjectMocks
    private GrievanceServiceImpl grievanceService;

    @Captor
    private ArgumentCaptor<GrievanceEntity> entityCaptor;

    private GrievanceRequestDto validRequest;
    private GrievanceEntity mockEntity;
    private static final String TEST_ACK_NO = "ACK1234567890";
    private static final String TEST_USERNAME = "test.user@example.com";


    @BeforeEach
    void setUp() {
        // Setup valid request DTO
        validRequest = GrievanceRequestDto.builder()
                .acknowledgementNo(TEST_ACK_NO)
                .insuranceCompany("HDFC Life Insurance")
                .emailSubject("Policy claim issue")
                .emailId("customer@example.com")
                .receivedFrom("John Doe")
                .grievanceType("Claim Related")
                .status(GrievanceStatus.RECEIVED.getStatus())
                .mode(ModeConstant.GMAIL.getStatus())
                .build();

        // Setup mock entity
        mockEntity = GrievanceEntity.builder()
                .id(1)
                .acknowledgementNo(TEST_ACK_NO)
                .insuranceCompany("HDFC Life Insurance")
                .emailSubject("Policy claim issue")
                .emailId("customer@example.com")
                .receivedFrom("John Doe")
                .grievanceType("Claim Related")
                .status(GrievanceStatus.RECEIVED.getStatus())
                .mode(ModeConstant.GMAIL.getStatus())
                .createdBy(TEST_USERNAME)
                .receivedOn(LocalDateTime.now())
                .build();
    }

    // Test case for successful grievance when no existing grievance is found
    @Test
    @DisplayName("Should successfully register new auto grievance when acknowledgement number does not exist")
    void registerAutoGrievance_WhenValidRequestAndNoExistingGrievance_ShouldCreateNewGrievanceSuccessfully() {

        // GIVEN - Setup test data and mock behaviors
        // Mock repository to return empty (no existing grievance)
        when(grievanceRepository.findByAcknowledgementNoAndStatusIsNotNullAndStatus(
                validRequest.getAcknowledgementNo(),
                GrievanceStatus.RECEIVED.getStatus()
        )).thenReturn(Optional.empty());

        // Mock mapper to convert DTO to Entity
        when(mapper.mapToEntity(validRequest)).thenReturn(mockEntity);

        // Mock SecurityUtils static method
        try (MockedStatic<SecurityUtils> securityUtilsMock = mockStatic(SecurityUtils.class)) {
            securityUtilsMock.when(SecurityUtils::getAuthenticatedUsername)
                    .thenReturn(TEST_USERNAME);

            // Mock repository save operation
            when(grievanceRepository.save(any(GrievanceEntity.class))).thenReturn(mockEntity);


            // WHEN - Execute the method under test
            ResponseEntity<ResponseData> actualResponse =
                    grievanceService.receivedGrievance(validRequest);


            // THEN - Verify the results and interactions
            // 1. Verify response is not null
            assertThat(actualResponse).isNotNull();

            // 2. Verify HTTP status code
            assertThat(actualResponse.getStatusCode())
                    .isEqualTo(HttpStatus.CREATED);

            // 3. Verify response body
            ResponseData responseBody = actualResponse.getBody();
            assertThat(responseBody).isNotNull();
            assertThat(responseBody.getStatus()).isEqualTo(Constants.SUCCESS);
            assertThat(responseBody.getMessage()).isEqualTo(Constants.SUCCESS_MESSAGE);

            // 4. Verify response data contains acknowledgement number
            assertThat(responseBody.getData()).isInstanceOf(Map.class);
            @SuppressWarnings("unchecked")
            Map<String, String> dataMap = (Map<String, String>) responseBody.getData();
            assertThat(dataMap).containsKey("acknowledgementNo");
            assertThat(dataMap.get("acknowledgementNo")).startsWith("ACK");

            // 5. Verify repository interactions
            verify(grievanceRepository, times(1))
                    .findByAcknowledgementNoAndStatusIsNotNullAndStatus(
                            validRequest.getAcknowledgementNo(),
                            GrievanceStatus.RECEIVED.getStatus()
                    );

            // 6. Verify save was called and capture the entity
            verify(grievanceRepository, times(1)).save(entityCaptor.capture());
            GrievanceEntity savedEntity = entityCaptor.getValue();

            // 7. Verify saved entity properties
            assertThat(savedEntity.getAcknowledgementNo()).isNotNull();
            assertThat(savedEntity.getAcknowledgementNo()).startsWith("ACK");
            assertThat(savedEntity.getCreatedBy()).isEqualTo(TEST_USERNAME);

            // 8. Verify mapper interactions
            verify(mapper, times(1)).mapToEntity(validRequest);

            // 9. Verify SecurityUtils was called
            securityUtilsMock.verify(SecurityUtils::getAuthenticatedUsername, times(1));
        }
    }


    @Test
    @DisplayName("Should throw RecordsAlreadyExistsException when existing grievance is found")
    void registerAutoGrievance_WhenExistingGrievance_ShouldThrowException() {
        // GIVEN
        when(grievanceRepository.findByAcknowledgementNoAndStatusIsNotNullAndStatus(
                validRequest.getAcknowledgementNo(),
                GrievanceStatus.RECEIVED.getStatus()
        )).thenReturn(Optional.of(mockEntity));

        // WHEN & THEN
        assertThatThrownBy(() -> grievanceService.receivedGrievance(validRequest))
                .isInstanceOf(RecordsAlreadyExistsException.class)
                .hasMessage("This is Already Available");

        verify(grievanceRepository, times(1))
                .findByAcknowledgementNoAndStatusIsNotNullAndStatus(
                        validRequest.getAcknowledgementNo(),
                        GrievanceStatus.RECEIVED.getStatus()
                );

        verify(grievanceRepository, never()).save(any(GrievanceEntity.class));
        verify(mapper, never()).mapToEntity(any(GrievanceRequestDto.class));
    }

}