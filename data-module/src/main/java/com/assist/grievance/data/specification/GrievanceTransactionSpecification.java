package com.assist.grievance.data.specification;

import com.assist.grievance.data.entity.GrievanceTransactionEntity;
import com.assist.grievance.data.enums.GrievanceStatus;
import com.assist.grievance.data.model.request.RegisterTxnGrievancePageRequest;
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
public class GrievanceTransactionSpecification {


    public Specification<GrievanceTransactionEntity> build(RegisterTxnGrievancePageRequest request) {
        log.info("build GrievanceTransactionSpecification with request: {}", request);
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (isValid(request.getGrievanceNo())) {
                predicates.add(cb.like(cb.lower(root.get("grievanceNumber")), "%" + request.getGrievanceNo().toLowerCase() + "%"));
            }
            if (isValid(request.getAcknowledgementNo())) {
                predicates.add(cb.like(cb.lower(root.get("acknowledgementNo")), "%" + request.getAcknowledgementNo().toLowerCase() + "%"));
            }
            if (isValid(request.getGrievanceTxnNo())) {
                predicates.add(cb.like(cb.lower(root.get("grievanceTxnNo")), "%" + request.getGrievanceTxnNo().toLowerCase() + "%"));
            }
            predicates.add(cb.equal(root.get("status"), GrievanceStatus.REJISTERED.getStatus()));
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }


    private static boolean isValid(String value) {
        return StringUtils.hasText(value) && !"string".equalsIgnoreCase(value);
    }
}
