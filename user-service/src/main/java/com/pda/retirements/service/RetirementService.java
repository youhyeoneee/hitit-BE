package com.pda.retirements.service;

import com.pda.investment_test.jpa.user_answer.UserAnswer;
import com.pda.retirements.dto.RetirementTestRequestDto;
import com.pda.retirements.dto.RetirementTestResponseDto;
import com.pda.retirements.jpa.Gender;
import com.pda.retirements.jpa.RetirementTestResult;
import com.pda.retirements.jpa.RetirementTestResultRepository;
import com.pda.retirements.jpa.RetirementType;
import com.pda.user_service.jpa.User;
import com.pda.user_service.jpa.UserRepository;
import com.pda.utils.exception.investment_tests.UserAnswerNotFoundException;
import com.pda.utils.exception.login.NotFoundUserException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class RetirementService {

    private UserRepository userRepository;
    private RetirementTestResultRepository retirementTestResultRepository;

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

    public Map<Integer, Long> calculateRemains(int startAge, int retirementAge, double monthlyLivingExpenses,
                                                     double totalFinancialAssets, double expectedInvestmentReturn) {
        Map<Integer, Long> remains = new HashMap<>();
        double annualInvestmentReturn = expectedInvestmentReturn / 100;
        double annualExpense = monthlyLivingExpenses * 12 * 10_000; // 만 단위
        Long wealth = (long) totalFinancialAssets * 100_000_000; // 억 단위

        log.info("Start age: " + startAge + ", Retirement age: " + retirementAge + ", Wealth: " + wealth);

        // 은퇴 나이 이전
        for (int age = startAge; age < retirementAge; age++) {
            long roundedValue = (long) Math.ceil(wealth * annualInvestmentReturn);
            wealth += roundedValue;
            log.info("Age " + age + " : " + wealth);
            remains.put(age, wealth);
        }

        // 은퇴 나이 이후
        for (int age = retirementAge; age <= 100; age++) {
            wealth += (long) Math.ceil(wealth * annualInvestmentReturn);
            wealth -= (int) annualExpense;
            log.info("Age " + age + " : " + wealth);
            remains.put(age, wealth);
            if (wealth <= 0) {
                break;
            }
        }

        return remains;
    }

    public int getAssetLife(Map<Integer, Long> remains) {
        for (Map.Entry<Integer, Long> entry : remains.entrySet()) {
            log.info(entry.getKey() + " : " + entry.getValue());
            if (entry.getValue() <= 0) {
                return entry.getKey();
            }
        }
        return 100;
    }


    public double findOptimalMonthlyLivingExpenses(int startAge, int retirementAge, double monthlyLivingExpenses,
                                                   double totalFinancialAssets, double expectedInvestmentReturn) {
        double low = 0;
        double high = monthlyLivingExpenses * 10_000;
        double tolerance = 1; // 허용 오차 (원하는 정확도에 따라 조정 가능)

        while (high - low > tolerance) {
            double mid = (low + high) / 2;
            Map<Integer, Long> remains = calculateRemains(startAge, retirementAge, mid, totalFinancialAssets, expectedInvestmentReturn);
            int assetLife = getAssetLife(remains);

            if (assetLife > retirementAge) {
                high = mid; // 생활비를 낮추어야 함
            } else {
                low = mid; // 생활비를 높여야 함
            }
        }

        return (low + high) / 2;
    }


    public RetirementTestResult saveResult(int userId, RetirementTestResponseDto testResponseDto) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundUserException(userId + "번 유저를 찾을 수 없습니다."));

        Optional<RetirementTestResult> savedResult = retirementTestResultRepository.findByUserId(userId);

        if (savedResult.isEmpty()) {
            RetirementTestResult result = new RetirementTestResult(userId, testResponseDto);
            retirementTestResultRepository.save(result);

            return result;
        } else {
            RetirementTestResult result = savedResult.get();
            result.setResults(testResponseDto);
            result.setUpdatedAt(LocalDateTime.now());
            retirementTestResultRepository.save(result);

            return result;
        }
    }

    public RetirementTestResult getResult(int userId) throws UserAnswerNotFoundException {
        return retirementTestResultRepository.findByUserId(userId)
                .orElseThrow(() -> new UserAnswerNotFoundException(userId + "번 유저의 저장된 테스트 결과가 없습니다."));
    }
}
