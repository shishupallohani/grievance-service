package com.assist.grievance.validator;


import com.assist.grievance.data.model.request.GrievanceRequestDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class GrievanceValidator {

    private static final String MEMBER_PORTAL = "member portal";
    private static final String CLAIM_DEPARTMENT = "claim";

    public List<String> validate(GrievanceRequestDto request) {
        List<String> errors = new ArrayList<>();

        if (request.getMode() != null) {
            // MEMBER_PORTAL validation
            if (MEMBER_PORTAL.equalsIgnoreCase(request.getMode().trim())) {
                if (request.getInsuranceCompany() == null ||
                        request.getInsuranceCompany().trim().isEmpty()) {
                    errors.add("Insurance Company is mandatory for Member Portal mode");
                }
                if (request.getDepartment() == null || request.getDepartment().trim().isEmpty()) {
                    errors.add("Department is mandatory for Member Portal mode");
                }
            }
        }

        if (request.getDepartment() != null) {
            if (CLAIM_DEPARTMENT.equalsIgnoreCase(request.getDepartment().trim())) {
                if (request.getClaimNo() == null ||
                        request.getClaimNo().trim().isEmpty()) {
                    errors.add("Claim No is mandatory for Claim department");
                }
            }
        }
        return errors;
    }
}
