package com.assist.grievance.data.model.request;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class GrievancePaginatedRequest extends HitpaBasePageRequest {
    private String screenType;
    private String insuranceCompany;
    private String grievanceType;
    private String receivedFrom;
    private String grievanceNo;
    private String ackNo;
    private String mode;
}
