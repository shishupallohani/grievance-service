package com.assist.grievance.data.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class GrievanceCategorizationPaginatedRequest extends HitpaBasePageRequest{
    private String grievanceNumber;
    private String policyType;
    private String grievanceType;
    private String receivedType;
}
