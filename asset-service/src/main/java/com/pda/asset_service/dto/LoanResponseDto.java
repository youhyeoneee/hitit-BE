package com.pda.asset_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanResponseDto {
    private String companyName;

    private String loanType;

    private int loanAmount;

    private Double interestRate;

    private int totalPayments;

    private String accountNo;

    private int userId;
}
