package com.assist.grievance.data.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GrievanceSummaryDto {

    private String mode;
    private Long totalCount;
    private Long processedCount;
    private Long pendingCount;
}