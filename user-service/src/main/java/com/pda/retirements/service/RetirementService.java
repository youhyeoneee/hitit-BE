package com.pda.retirements.service;

import com.pda.retirements.dto.RetirementTestRequestDto;
import com.pda.retirements.jpa.RetirementType;
import org.springframework.stereotype.Service;

@Service
public class RetirementService {

    // 노후준비종합진단 - 재무준비 여건 및 인식
    // 예상 은퇴 연령, 소득활동 지속 노력, 노후 예상 생활비
    public double calculateArea1Score(int retirementAge, int careerEffortScore, int monthlyLivingExpenses) {
        double score = (retirementAge * 0.3 + careerEffortScore * 0.432 + monthlyLivingExpenses * 0.268) * 20;
        return score;
    }

    public double calculateArea2Score(int expectedNationalPension, int totalFinancialAssets, int totalRealEstateValue) {
        double score = (expectedNationalPension + totalFinancialAssets + totalRealEstateValue) * (100/15);
        return score;
    }

    public int getScore(RetirementTestRequestDto testRequestDto) {
        double area1Score = calculateArea1Score(
                testRequestDto.getRetirementAge(),
                testRequestDto.getCareerEffortScore(),
                testRequestDto.getMonthlyLivingExpenses()
        );


        double area2Score = calculateArea2Score(
                testRequestDto.getExpectedNationalPension(),
                testRequestDto.getTotalFinancialAssets(),
                testRequestDto.getTotalRealEstateValue()
        );

        return (int) (area1Score * 0.319 + area2Score * 0.681);
    }

    public RetirementType getRetirementType(int score) {
        if (score <= 5505) {
            return RetirementType.LEVEL_1;
        } else if (score <= 10880) {
            return RetirementType.LEVEL_2;
        } else if (score <= 16355) {
            return RetirementType.LEVEL_3;
        } else {
            return RetirementType.LEVEL_4;
        }
    }
}
