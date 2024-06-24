package com.pda.asset_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PensionDto {
    @JsonProperty("company_name")
    private String companyName;

    @JsonProperty("pension_name")
    private String pensionName;

    @JsonProperty("pension_type")
    private String pensionType;

    @JsonProperty("interest_rate")
    private Double interestRate;

    @JsonProperty("evaluation_amount")
    private String evaluationAmount;

    @JsonProperty("expiration_date")
    private Date expirationDate;

    @JsonProperty("account_no")
    private String accountNo;

    @JsonProperty("retirement_pension_claimed")
    private Integer retirementPensionClaimed; // 퇴직 연금 청구 여부

    @JsonProperty("user_id")
    private int userId;
}
