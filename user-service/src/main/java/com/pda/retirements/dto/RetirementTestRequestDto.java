package com.pda.retirements.dto;


import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class RetirementTestRequestDto {
    // 예상 은퇴 연령 : 20 ~ 99
    // input : 20 ~ 99
    private int retirementAge;
    // 소득활동 지속 노력 : 1 ~ 5
    // input : 1 ~ 5
    private int careerEffortScore;
    // 노후 예상 월 생활비 : 50만원 ~ 9999만원
    // input : 50 ~ 9999
    private int monthlyLivingExpenses;
    // 국민연금 예상 수령액 : 0원 ~ 1000만원
    // input : 0 ~ 1000
    private int expectedNationalPension;
    // 금융 자산 : 1억원 ~ 50억원
    // input : 1 ~ 50
    private int totalFinancialAssets;
    // 부동산 자산 : 0억원 ~ 10억원
    // input : 0 ~ 10
    private int totalRealEstateValue;
    // 예상 투자 수익률 : 0% ~ 20%
    // input : 0 ~ 20
    private int expectedInvestmentReturn;
}
