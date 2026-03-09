package com.assist.grievance.data.specification;


import com.assist.grievance.data.entity.GrievanceRegistrationEntity;
import com.assist.grievance.data.enums.GrievanceStatus;
import com.assist.grievance.data.model.request.RegisterGrievancePaginatedRequest;
import jakarta.persistence.criteria.Predicate;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
@Slf4j
public class GrievanceRegisterSpecification {


    public Specification<GrievanceRegistrationEntity> build(RegisterGrievancePaginatedRequest request) {
        log.info("Build GrievanceRegisterSpecification with request: {}", request);
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (isValid(request.getGrievanceType())) {
                predicates.add(cb.like(cb.lower(root.get("grievanceType")), "%" + request.getGrievanceType().toLowerCase() + "%"));
            }
            if (isValid(request.getInsuranceCompanyName())) {
                predicates.add(cb.like(cb.lower(root.get("insuranceCompanyName")), "%" + request.getInsuranceCompanyName().toLowerCase() + "%"));
            }
            if (isValid(request.getPolicyType())) {
                predicates.add(cb.like(cb.lower(root.get("policyType")), "%" + request.getPolicyType().toLowerCase() + "%"));
            }
            if (isValid(request.getReceivedFrom())) {
                predicates.add(cb.like(cb.lower(root.get("receivedFrom")), "%" + request.getReceivedFrom().toLowerCase() + "%"));
            }
            if (isValid(request.getGrievanceNumber())) {
                predicates.add(cb.like(cb.lower(root.get("grievanceNumber")), "%" + request.getGrievanceNumber().toLowerCase() + "%"));
            }

            // =======================
            // DATE FILTER LOGIC
            // =======================
            LocalDateTime fromDate = request.getFromDate();
            LocalDateTime toDate = request.getToDate();

            if (fromDate != null && toDate != null) {

                // Swap if fromDate is after toDate
                if (fromDate.isAfter(toDate)) {
                    LocalDateTime temp = fromDate;
                    fromDate = toDate;
                    toDate = temp;
                }

                predicates.add(cb.between(root.get("createdDate"), fromDate, toDate));
                // Handle Only fromDate
            } else if (fromDate != null) {

                predicates.add(cb.greaterThanOrEqualTo(root.get("createdDate"), fromDate));

                // Handle Only toDate
            } else if (toDate != null) {

                predicates.add(cb.lessThanOrEqualTo(root.get("createdDate"), toDate));
            }

            predicates.add(cb.equal(root.get("status"), GrievanceStatus.REJISTERED.getStatus()));
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }


    private static boolean isValid(String value) {
        return StringUtils.hasText(value) && !"string".equalsIgnoreCase(value);
    }
}
