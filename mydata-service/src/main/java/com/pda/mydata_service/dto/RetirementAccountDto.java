package com.pda.mydata_service.dto;

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
public class RetirementAccountDto {
    private String companyName;

    private String pensionName;

    private String pensionType;

    private Double interestRate;

    private String evaluationAmount;

    private Date expirationDate;

    private String accountNo;

    private String retirementPensionClaimed; // 퇴직 연금 청구 여부

    private int userId;

    private int balance;
}
