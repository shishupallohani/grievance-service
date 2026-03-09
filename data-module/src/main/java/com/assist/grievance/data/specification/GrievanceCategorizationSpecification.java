package com.assist.grievance.data.specification;


import com.assist.grievance.data.entity.GrievanceCategorizationEntity;
import com.assist.grievance.data.model.request.GrievanceCategorizationPaginatedRequest;
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
public class GrievanceCategorizationSpecification {


    public Specification<GrievanceCategorizationEntity> build(GrievanceCategorizationPaginatedRequest request) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (isValid(request.getPolicyType())) {
                predicates.add(cb.like(cb.lower(root.get("policyType")), "%" + request.getPolicyType().toLowerCase() + "%"));
            }
            if (isValid(request.getGrievanceNumber())) {
                predicates.add(cb.like(cb.lower(root.get("grievanceNumber")), "%" + request.getGrievanceNumber().toLowerCase() + "%"));
            }
            if (isValid(request.getGrievanceType())) {
                predicates.add(cb.like(cb.lower(root.get("grievanceType")), "%" + request.getGrievanceType().toLowerCase() + "%"));
            }
            if (isValid(request.getReceivedType())) {
                predicates.add(cb.like(cb.lower(root.get("receivedType")), "%" + request.getReceivedType().toLowerCase() + "%"));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }


    private static boolean isValid(String value) {
        return StringUtils.hasText(value) && !"string".equalsIgnoreCase(value);
    }
}
