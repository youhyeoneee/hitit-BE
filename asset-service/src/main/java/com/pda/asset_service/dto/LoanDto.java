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
public class LoanDto {
    @JsonProperty("company_name")
    private String companyName;

    @JsonProperty("loan_type")
    private String loanType;

    @JsonProperty("loan_amount")
    private int loanAmount;

    @JsonProperty("interest_rate")
    private Double interestRate;

    @JsonProperty("total_payments")
    private int totalPayments;

    @JsonProperty("account_no")
    private String accountNo;

    @JsonProperty("user_id")
    private int userId;
}
