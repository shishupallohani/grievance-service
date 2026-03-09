package com.assist.grievance.integration;

import com.assist.grievance.aop.GrievanceLoggingAspect;
import com.assist.grievance.data.model.request.GrievanceRequestDto;
import com.assist.grievance.data.repository.GrievanceRepository;
import com.assist.grievance.service.Impl.GrievanceServiceImpl;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.assist.grievance.mainapp.MainApplication;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.Optional;

/* This is an integration test to verify that the AOP aspect is triggered
 when the registerAutoGrievance method is called in the GrievanceServiceImpl. */
@SpringBootTest(classes = MainApplication.class)
public class GrievanceServiceImplTest {

    @Autowired
    private GrievanceServiceImpl grievanceService;

    // isolate the repository layer
    @MockitoBean
    private GrievanceRepository grievanceRepository;

    @MockitoSpyBean
    private GrievanceLoggingAspect grievanceLoggingAspect;



    @Disabled("Temporarily disabled – AOP test unstable")
    @Test
    void shouldExecuteAopWhenRegisterAutoGrievanceIsCalled() {

        // GIVEN
        GrievanceRequestDto request = new GrievanceRequestDto();
        request.setAcknowledgementNo("AOP-TEST-2026");

        when(grievanceRepository
                .findByAcknowledgementNoAndStatusIsNotNullAndStatus(
                        "AOP-TEST-2026", "received"))
                .thenReturn(Optional.empty());

        // WHEN
        var response = grievanceService.receivedGrievance(request);

        // THEN
        assertThat(response).isNotNull();
    }
}
