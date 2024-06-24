package com.pda.retirements.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.pda.retirements.jpa.RetirementType;
import com.pda.utils.api_utils.CustomStringUtils;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;


@Getter
@Setter
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class RetirementTestResponseDto {
    private ArrayList<String> type;
    private ArrayList<String> monthlyLivingExpenses;
    private String totalFinancialAssets;
    private String expectedNationalPension;
    private String totalRealEstateValue;
    private String careerEffortScore;
    private int assetLife;
    private int lifeExpectancy;
    private int optimalMonthlyLivingExpenses;

    public RetirementTestResponseDto(RetirementType retirementType, int monthlyLivingExpenses,  int totalFinancialAssets,
                                    int expectedNationalPension, int totalRealEstateValue, int careerEffortScore) {
        this.type = new ArrayList<>(Arrays.asList(String.valueOf(retirementType.getLevel()), retirementType.getName(), retirementType.getDescription()));
        this.monthlyLivingExpenses = getMonthlyLivingExpensesResult(monthlyLivingExpenses);
        this.totalFinancialAssets = getTotalFinancialAssetsResult(totalFinancialAssets);
        this.expectedNationalPension = getExpectedNationalPensionResult(expectedNationalPension);
        this.totalRealEstateValue = getTotalRealEstateValueResult(totalRealEstateValue);
        this.careerEffortScore = getCareerEffortScoreResult(careerEffortScore);
    }

    private String getCareerEffortScoreResult(int careerEffortScore) {
        if (careerEffortScore <= 2) {
            return "부족";
        } else if (careerEffortScore == 3) {
            return "적정";
        } else {
            return "여유";
        }
    }


    // TODO: 연령대별 평균 구하기
    private String getTotalRealEstateValueResult(int totalRealEstateValue) {
        if (totalRealEstateValue <= 5000) {
            return "부족";
        } else if (totalRealEstateValue < 10000) {
            return "적정";
        } else {
            return "여유";
        }
    }

    // TODO: 연령대별 평균 구하기
    private String getExpectedNationalPensionResult(int expectedNationalPension) {
        if (expectedNationalPension <= 60) {
            return "부족";
        } else if (expectedNationalPension < 65) {
            return "적정";
        } else {
            return "여유";
        }
    }


    // TODO: 연령대별 평균 구하기
    private String getTotalFinancialAssetsResult(int totalFinancialAssets) {
        if (totalFinancialAssets <= 5000) {
            return "부족";
        } else if (totalFinancialAssets < 10000) {
            return "적정";
        } else {
            return "여유";
        }
    }

    private ArrayList<String> getMonthlyLivingExpensesResult(int monthlyLivingExpenses) {
        ArrayList<String> result = new ArrayList<>();
        int expectedMonthlyRetirementExpenses = 177; // 예상 월 노후 생활비 통계
        int diff = Math.abs(monthlyLivingExpenses - expectedMonthlyRetirementExpenses);
        String formattedAverageExpenses = CustomStringUtils.formatMoney(expectedMonthlyRetirementExpenses);
        String formattedDifference = CustomStringUtils.formatMoney(diff);

        if (monthlyLivingExpenses > expectedMonthlyRetirementExpenses) {
            result.add("여유");
            result.add(formattedAverageExpenses + "만원보다 " + formattedDifference + "만원 많음");
        } else if (monthlyLivingExpenses == expectedMonthlyRetirementExpenses) {
            result.add("적정");
            result.add(formattedAverageExpenses + "만원과 같음");
        } else {
            result.add("부족");
            result.add(formattedAverageExpenses + "만원보다 " + formattedDifference + "만원 적음");
        }
        return result;
    }


}

