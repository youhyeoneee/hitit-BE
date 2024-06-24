package com.pda.retirements.controller;

import com.pda.retirements.dto.RetirementTestRequestDto;
import com.pda.retirements.dto.RetirementTestResponseDto;
import com.pda.retirements.jpa.RetirementTestResult;
import com.pda.retirements.jpa.RetirementType;
import com.pda.retirements.service.RetirementService;
import com.pda.auth.security.JwtTokenProvider;
import com.pda.user_service.jpa.User;
import com.pda.user_service.service.UserService;
import com.pda.utils.api_utils.ApiUtils;
import com.pda.utils.api_utils.CustomStringUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.pda.utils.api_utils.ApiUtils.success;

@RestController
@Slf4j
@RequestMapping("/api/retirements")
@AllArgsConstructor
public class RetirementController {
    private UserService userService;
    private RetirementService retirementService;
    private JwtTokenProvider jwtTokenProvider;

    @PostMapping("/test/results")
    public ApiUtils.ApiResult getTestResults(@RequestBody RetirementTestRequestDto testRequestDto,  @RequestHeader("Authorization") String bearerToken) {
        // 토큰 -> user id
        log.info("bearerToken : " + bearerToken);
        String token = CustomStringUtils.getToken(bearerToken);
        int userId = Integer.parseInt(jwtTokenProvider.getUsername(token));
        log.info("user id : " + userId);

        // 점수 계산
        int score = retirementService.getScore(testRequestDto);

        // 유형 판단
        RetirementType retirementType = retirementService.getRetirementType(score);
        RetirementTestResponseDto testResponseDto = new RetirementTestResponseDto(
                retirementType,
                testRequestDto.getMonthlyLivingExpenses(),
                testRequestDto.getTotalFinancialAssets(),
                testRequestDto.getExpectedNationalPension(),
                testRequestDto.getTotalRealEstateValue(),
                testRequestDto.getCareerEffortScore()
                );


        // 유저 조회해서 나이, 성별 알기
        User user = userService.findUserById(userId);
        int age = CustomStringUtils.calculateAge(user.getBirthdate());
        String gender = user.getGender();

        int lifeExpectancy = retirementService.calculateLifeExpectancy(age, gender);
        testResponseDto.setLifeExpectancy(lifeExpectancy);

        // 현재 나이, 은퇴 나이, 월 생활비, 금융 자산, 예상 투자 수익률
        Map<Integer, Long> remains = retirementService.calculateRemains(age, testRequestDto.getRetirementAge(),
                testRequestDto.getMonthlyLivingExpenses(), testRequestDto.getTotalFinancialAssets(), testRequestDto.getExpectedInvestmentReturn());

        int assetLife = retirementService.getAssetLife(remains);
        testResponseDto.setAssetLife(assetLife);

        int optimalMonthlyLivingExpenses = (int) ((retirementService.findOptimalMonthlyLivingExpenses(age, testRequestDto.getRetirementAge(),
                        testRequestDto.getMonthlyLivingExpenses(), testRequestDto.getTotalFinancialAssets(), testRequestDto.getExpectedInvestmentReturn())) / 10_000);
        testResponseDto.setOptimalMonthlyLivingExpenses(optimalMonthlyLivingExpenses);

        // DB에 결과 저장
        retirementService.saveResult(userId, testResponseDto);
        return success(testResponseDto);
    }

    @GetMapping("/test/results")
    public ApiUtils.ApiResult getTestResults(@RequestHeader("Authorization") String bearerToken) {
        // 토큰 -> user id
        log.info("bearerToken : " + bearerToken);
        String token = CustomStringUtils.getToken(bearerToken);
        int userId = Integer.parseInt(jwtTokenProvider.getUsername(token));
        log.info("user id : " + userId);

        RetirementTestResult result = retirementService.getResult(userId);
        return success(result);
    }


}
