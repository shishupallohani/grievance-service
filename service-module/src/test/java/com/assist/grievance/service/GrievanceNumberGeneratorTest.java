package com.assist.grievance.service;

import com.assist.grievance.Util.GrievanceNumberGenerator;
import com.assist.grievance.data.entity.GrievanceSequenceEntity;
import com.assist.grievance.data.repository.GrievanceSequenceRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class GrievanceNumberGeneratorTest {

    @Mock
    private GrievanceSequenceRepository sequenceRepository;

    @InjectMocks
    private GrievanceNumberGenerator generator;


    /*  1. Invalid input -> exception expected
        - Input: null company code
        - Expected: IllegalArgumentException
        2. Existing sequence -> increment & format
        - Input: company code with existing sequence record
        - Expected: grievance number with incremented sequence
        3. No sequence record -> create new sequence
        - Input: company code with no existing sequence record
        - Expected: grievance number starting at 00001
     */
    // TEST CASE 1.Invalid input -> exception expected
    @Test
    @DisplayName("Should throw IllegalArgumentException when company code is null")
    @Disabled
    void generateGrievanceNumber_shouldThrowException_whenCompanyIsNull() {

        assertThatThrownBy(() -> generator.generateGrievanceNumber(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Insurance company code cannot be null");
    }


    // TEST CASE 2.Existing sequence -> increment & format
    @Test
    @DisplayName("Should increment sequence when record exists")
    @Disabled
    void generateGrievanceNumber_shouldIncrementSequence_whenRecordExists() {

        // GIVEN
        GrievanceSequenceEntity existing = new GrievanceSequenceEntity();
        existing.setInsuranceCompany("NIC");
        existing.setFinancialYear("2025");
        existing.setLastSequence(12);

        when(sequenceRepository.findGrievanceByNumberWithLock(any()))
                .thenReturn(Optional.of(existing));

        when(sequenceRepository.save(any()))
                .thenAnswer(inv -> inv.getArgument(0));

        // WHEN
        String grievanceNumber = generator.generateGrievanceNumber("NIC");

        // THEN
        assertThat(grievanceNumber)
                .startsWith("GRNIC")
                .endsWith("00013");

        verify(sequenceRepository).save(existing);
    }


    //TEST CASE 3.No sequence record -> create new sequence
    @Test
    @Disabled
    void generateGrievanceNumber_shouldCreateNewSequence_whenNoRecordExists() {

        // GIVEN
        when(sequenceRepository.findGrievanceByNumberWithLock(any()))
                .thenReturn(Optional.empty());

        when(sequenceRepository.save(any()))
                .thenAnswer(inv -> {
                    GrievanceSequenceEntity e = inv.getArgument(0);
                    e.setLastSequence(1);
                    return e;
                });

        // WHEN
        String grievanceNumber = generator.generateGrievanceNumber("NIA");

        // THEN
        assertThat(grievanceNumber)
                .startsWith("GRNIA")
                .endsWith("00001");

        verify(sequenceRepository).save(any(GrievanceSequenceEntity.class));
    }


    /* 1. Base grievance exists with No incidence -> increment incidence */
    @Test
    @DisplayName("Should generate transaction number when base grievance generate")
    @Disabled
    void generateGrievanceTransactionNumber_shouldIncrementIncidence_whenRecordExists() {

        // GIVEN
        String baseGrievanceNumber = "GRNIC202500013";

        GrievanceSequenceEntity existing = new GrievanceSequenceEntity();
        existing.setInsuranceCompany("NIC");
        existing.setFinancialYear("2025");
        existing.setBaseGrievanceNumber(baseGrievanceNumber);
        existing.setLastSequence(13);
        existing.setIncidenceCount(1); // already one incidence exists

        when(sequenceRepository.findGrievanceByNumberWithLock(baseGrievanceNumber))
                .thenReturn(Optional.of(existing));
       /* Basically we are mocking the save method to return the entity passed to it,
        which allows us to verify that the incidence count was incremented correctly. */
        when(sequenceRepository.save(any()))
                .thenAnswer(inv -> inv.getArgument(0));

        // WHEN
        String transactionNumber =
                generator.generateGrievanceTransactionNumber(baseGrievanceNumber);

        // THEN
        assertThat(transactionNumber)
                .isEqualTo("GRNIC202500013-02");

        assertThat(existing.getIncidenceCount()).isEqualTo(2);

        verify(sequenceRepository, times(1))
                .findGrievanceByNumberWithLock(baseGrievanceNumber);

        verify(sequenceRepository, times(1))
                .save(existing);
    }

    @Test
    @DisplayName("Should generate first transaction number when grievance number is new")
    @Disabled
    void generateGrievanceTransactionNumber_shouldCreateNewSequenceAndReturn01_whenFirstTime() {

        // GIVEN
        String baseGrievanceNumber = "GRNIC202500001";

        when(sequenceRepository.findGrievanceByNumberWithLock(baseGrievanceNumber))
                .thenReturn(Optional.empty());

         /* Basically we are mocking the save method to return the entity passed to it,
        which allows us to verify that the incidence count was incremented correctly. */
        when(sequenceRepository.save(any(GrievanceSequenceEntity.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        // WHEN
        String transactionNumber =
                generator.generateGrievanceTransactionNumber(baseGrievanceNumber);

        // THEN
        assertThat(transactionNumber)
                .isEqualTo("GRNIC202500001-01");

        verify(sequenceRepository, times(1))
                .findGrievanceByNumberWithLock(baseGrievanceNumber);

        // save() will be called:
        // 1. new sequence create
        // 2. after incidence increment
        verify(sequenceRepository, atLeastOnce())
                .save(any(GrievanceSequenceEntity.class));
    }

}

