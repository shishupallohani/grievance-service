package com.assist.grievance.data.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class BillEntryDto {

    private String claimNo;
    private String claimNoExt;
    private String claimTxnNo;
    private String claimTxnExt;

    private BigDecimal blockedAmount;

    private String providerName;
    private String patientName;
    private String claimType;

    private BigDecimal sumInsured;
    private String policyCode;

    private Boolean isVip;
    private Boolean isDeathCase;
}
