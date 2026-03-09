package com.assist.grievance.data.specification;

import com.assist.grievance.data.entity.GrievanceEntity;
import com.assist.grievance.data.enums.GrievanceStatus;
import com.assist.grievance.data.model.request.GrievancePaginatedRequest;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
@Slf4j
public class GrievanceSpecification {

    public Specification<GrievanceEntity> build(GrievancePaginatedRequest request) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (isValid(request.getGrievanceType())) {
                predicates.add(cb.like(cb.lower(root.get("grievanceType")), "%" + request.getGrievanceType().toLowerCase() + "%"));
            }
            if (isValid(request.getGrievanceNo())) {
                predicates.add(cb.like(cb.lower(root.get("grievanceNo")), "%" + request.getGrievanceNo().toLowerCase() + "%"));
            }
            if (isValid(request.getInsuranceCompany())) {
                predicates.add(cb.like(cb.lower(root.get("insuranceCompany")), "%" + request.getInsuranceCompany().toLowerCase() + "%"));
            }
            if (isValid(request.getAckNo())) {
                predicates.add(cb.like(cb.lower(root.get("acknowledgementNo")), "%" + request.getAckNo().toLowerCase() + "%"));
            }
            if (isValid(request.getReceivedFrom())) {
                predicates.add(cb.like(cb.lower(root.get("receivedFrom")), "%" + request.getReceivedFrom().toLowerCase() + "%"));
            }
            if (isValid(request.getMode())) {
                predicates.add(cb.like(cb.lower(root.get("mode")), "%" + request.getMode().toLowerCase() + "%"));
            }

            if (isValid(request.getScreenType())) {
                switch (request.getScreenType().toUpperCase()) {
                    case "NG":
                        predicates.add(cb.equal(root.get("status"), GrievanceStatus.RECEIVED.getStatus()));
                        break;
                    case "RG":
                        predicates.add(cb.equal(root.get("status"), GrievanceStatus.REJISTERED.getStatus()));
                        break;
                    case "TG":
                        CriteriaBuilder.In<String> inClause = cb.in(root.get("status"));
                        inClause.value(GrievanceStatus.REJISTERED.getStatus());
                        inClause.value(GrievanceStatus.PENDING.getStatus());
                        predicates.add(inClause);
                        break;
                }
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private static boolean isValid(String value) {
        return StringUtils.hasText(value) && !"string".equalsIgnoreCase(value);
    }
}
