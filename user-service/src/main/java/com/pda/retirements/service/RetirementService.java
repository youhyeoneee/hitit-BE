package com.pda.retirements.service;

import com.pda.retirements.dto.RetirementTestRequestDto;
import com.pda.retirements.jpa.Gender;
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

    public int calculateLifeExpectancy(int age, String gender) {
        if (gender.equalsIgnoreCase(Gender.F.getName())) {
            return getFemaleLifeExpectancy(age);
        } else if (gender.equalsIgnoreCase(Gender.M.getName())) {
            return getMaleLifeExpectancy(age);
        } else {
            throw new IllegalArgumentException("성별이 유효하지 않습니다. "
                    + Gender.M.getName() + "혹은" + Gender.F.getName() + "이어야합니다.");
        }
    }

    private static int getMaleLifeExpectancy(int age) {
        if (age >= 0 && age <= 32) {
            return 81;
        } else if (age <= 38) {
            return 82;
        } else if (age <= 43) {
            return 83;
        } else if (age <= 49) {
            return 84;
        } else if (age <= 53) {
            return 85;
        } else if (age <= 58) {
            return 86;
        } else if (age <= 64) {
            return 87;
        } else if (age <= 69) {
            return 88;
        } else if (age <= 74) {
            return 89;
        } else if (age <= 78) {
            return 90;
        } else if (age <= 80) {
            return 91;
        } else if (age <= 87) {
            return 92;
        } else if (age <= 89) {
            return 93;
        } else if (age <= 93) {
            return 94;
        } else if (age == 95) {
            return 95;
        } else if (age == 97) {
            return 96;
        } else if (age == 99) {
            return 97;
        } else if (age >= 100) {
            return 102;
        } else {
            throw new IllegalArgumentException("유효하지 않은 나이입니다. 나이는 0보다 같거나 커야합니다.");
        }
    }

    private static int getFemaleLifeExpectancy(int age) {
        if (age <= 49) {
            return 87;
        } else if (age <= 69) {
            return 88;
        } else if (age <= 74) {
            return 89;
        } else if (age <= 79) {
            return 90;
        } else if (age <= 81) {
            return 91;
        } else if (age <= 83) {
            return 92;
        } else if (age <= 85) {
            return 93;
        } else if (age <= 88) {
            return 94;
        } else if (age <= 90) {
            return 95;
        } else if (age <= 92) {
            return 96;
        } else if (age <= 94) {
            return 97;
        } else if (age == 95) {
            return 98;
        } else if (age == 96) {
            return 99;
        } else if (age == 97) {
            return 100;
        } else if (age == 98) {
            return 101;
        } else if (age >= 99) {
            return 102;
        } else {
            throw new IllegalArgumentException("유효하지 않은 나이입니다. 나이는 0보다 같거나 커야합니다.");
        }
    }

}
