package com.pda.mydata_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanDto {
    private String companyName;

    private String loanType;

    private int loanAmount;

    private Double interestRate;

    private int totalPayments;

    private String accountNo;

    private int userId;
}
