package com.pda.retirements.controller;

import com.pda.retirements.dto.RetirementTestRequestDto;
import com.pda.retirements.dto.RetirementTestResponseDto;
import com.pda.retirements.jpa.RetirementType;
import com.pda.retirements.service.RetirementService;
import com.pda.security.JwtTokenProvider;
import com.pda.user_service.jpa.User;
import com.pda.user_service.service.UserService;
import com.pda.utils.api_utils.ApiUtils;
import com.pda.utils.api_utils.CustomStringUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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

        
        return success(testResponseDto);
    }


}
