package com.assist.grievance.Util;


import com.assist.grievance.data.entity.GrievanceSequenceEntity;
import com.assist.grievance.data.repository.GrievanceSequenceRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Month;
import java.util.Optional;

@Component
@Slf4j
@AllArgsConstructor
public class GrievanceNumberGenerator {

    private final GrievanceSequenceRepository sequenceRepository;

    /**
     * Generate base grievance number
     */
    @Transactional
    public String generateGrievanceNumber(String insuranceCompany) {
        log.info("Inside generateGrievanceNumber");
        if (insuranceCompany == null || insuranceCompany.trim().isEmpty()) {
            throw new IllegalArgumentException("Insurance company code cannot be null or empty");
        }

        String companyCode = insuranceCompany.trim().toUpperCase();
        String financialYear = getCurrentFinancialYear();

        // STEP 1: Get or create COUNTER row
        GrievanceSequenceEntity counterRow = sequenceRepository
                .findCounterByCompanyAndYearWithLock(companyCode, financialYear)
                .orElseGet(() -> createCounterRow(companyCode, financialYear));

        // STEP 2: Increment sequence in COUNTER row
        counterRow.setLastSequence(counterRow.getLastSequence() + 1);
        int newSequence = counterRow.getLastSequence();
        sequenceRepository.save(counterRow);

        // STEP 3: Generate grievance number
        String baseGrievanceNumber = String.format("GR%s%s%05d",
                companyCode, financialYear, newSequence);

        // STEP 4: Create GRIEVANCE row for this new number
        GrievanceSequenceEntity grievanceRow = new GrievanceSequenceEntity();
        grievanceRow.setInsuranceCompany(companyCode);
        grievanceRow.setFinancialYear(financialYear);
        grievanceRow.setRecordType("GRIEVANCE");
        grievanceRow.setLastSequence(newSequence);
        grievanceRow.setBaseGrievanceNumber(baseGrievanceNumber);
        grievanceRow.setIncidenceCount(0);

        sequenceRepository.save(grievanceRow);

        log.info("Generated Base Grievance Number: {}", baseGrievanceNumber);
        return baseGrievanceNumber;
    }

    /**
     * Generate transaction/incidence number
     */
    @Transactional
    public String generateGrievanceTransactionNumber(String grievanceNumber) {
        log.info("Generating transaction number for: {}", grievanceNumber);

        // STEP 1: Fetch GRIEVANCE row with lock
        GrievanceSequenceEntity grievanceRow = sequenceRepository
                .findGrievanceByNumberWithLock(grievanceNumber)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Grievance number not found: " + grievanceNumber));

        // STEP 2: Increment incidence count
        grievanceRow.setIncidenceCount(grievanceRow.getIncidenceCount() + 1);
        int incidenceNumber = grievanceRow.getIncidenceCount();

        sequenceRepository.save(grievanceRow);

        // STEP 3: Generate transaction number
        String transactionNumber = String.format("%s-%02d",
                grievanceNumber, incidenceNumber);

        log.info("Generated Transaction Number: {}", transactionNumber);
        return transactionNumber;
    }

    /**
     * Create new COUNTER row
     */
    private GrievanceSequenceEntity createCounterRow(String companyCode, String financialYear) {
        GrievanceSequenceEntity counterRow = new GrievanceSequenceEntity();
        counterRow.setInsuranceCompany(companyCode);
        counterRow.setFinancialYear(financialYear);
        counterRow.setRecordType("COUNTER");
        counterRow.setLastSequence(0);
        counterRow.setIncidenceCount(0);
        counterRow.setBaseGrievanceNumber(null);
        return sequenceRepository.save(counterRow);
    }

    /**
     * Get current financial year
     */
    private String getCurrentFinancialYear() {
        LocalDate today = LocalDate.now();
        int year = today.getYear();

        if (today.getMonth().getValue() >= Month.APRIL.getValue()) {
            return String.valueOf(year);
        } else {
            return String.valueOf(year - 1);
        }
    }
}