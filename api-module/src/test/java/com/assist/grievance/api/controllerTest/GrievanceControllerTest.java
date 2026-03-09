package com.assist.grievance.api.controllerTest;

import com.assist.grievance.api.controller.GrievanceController;
import com.assist.grievance.commonUtil.Constants;
import com.assist.grievance.commonUtil.ResponseData;
import com.assist.grievance.data.enums.ModeConstant;
import com.assist.grievance.data.model.request.GrievanceRequestDto;
import com.assist.grievance.service.GrievanceService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.Collections;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(MockitoExtension.class)
class GrievanceControllerTest {

    private MockMvc mockMvc;

    @Mock
    private GrievanceService grievanceService;

    @InjectMocks
    private GrievanceController grievanceController;

    private ObjectMapper objectMapper;
    private GrievanceRequestDto validRequest;
    private ResponseData successResponse;
    private static final String API_ENDPOINT = "/api/v1/grievance/register";
    private static final String TEST_ACK_NO = "ACK1234567890";

    @BeforeEach
    void setUp() {
        // Setup MockMvc with validator (important for @Valid to work)
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();

        mockMvc = MockMvcBuilders.standaloneSetup(grievanceController)
                .setValidator(validator)  //Enable validation
                .setMessageConverters(new MappingJackson2HttpMessageConverter())  //JSON conversion
                .build();

        objectMapper = new ObjectMapper();

        // Setup valid request
        validRequest = GrievanceRequestDto.builder()
                .insuranceCompany("HDFC Life Insurance")
                .emailSubject("Policy claim issue")
                .emailId("customer@example.com")
                .receivedFrom("John Doe")
                .grievanceType("Claim Related")
                .status("received")
                .build();

        // Setup success response
        successResponse = ResponseData.builder()
                .message(Constants.SUCCESS_MESSAGE)
                .status(Constants.SUCCESS)
                .data(Map.of("acknowledgementNo", TEST_ACK_NO))
                .resource(Collections.emptyList())
                .build();
    }

    @Test
    @DisplayName("Should return bad request when department is missing")
    void createAutoGrievance_WhenMissingDepartment_ShouldReturnBadRequest() throws Exception {
        // GIVEN - Setup invalid request (missing insuranceCompany)
        GrievanceRequestDto invalidRequest = GrievanceRequestDto.builder()
                .acknowledgementNo(TEST_ACK_NO)
                .emailSubject("Policy claim issue")
                .emailId("customer@example.com")
                .receivedFrom("John Doe")
                .grievanceType("Claim Related")
                .status("received")
                .build();

        // WHEN & THEN - Execute POST request and verify validation error
        mockMvc.perform(post(API_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(Constants.VALIDATION_ERROR))
                .andExpect(jsonPath("$.message").value(Constants.VALIDATION_ERROR_MESSAGE))
                .andExpect(jsonPath("$.error").value("mode is mandatory"));

        // Service should NOT be called due to validation failure
        verify(grievanceService, never()).receivedGrievance(any(GrievanceRequestDto.class));
        verifyNoInteractions(grievanceService);
    }
}
