package com.assist.grievance.data.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class RegisterTxnGrievancePageRequest extends HitpaBasePageRequest {
    private String grievanceNo;
    private String grievanceTxnNo;
    private String acknowledgementNo;
}
