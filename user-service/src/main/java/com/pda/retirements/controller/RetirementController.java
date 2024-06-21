package com.pda.retirements.controller;

import com.pda.retirements.dto.RetirementTestRequestDto;
import com.pda.retirements.dto.RetirementTestResponseDto;
import com.pda.retirements.jpa.RetirementType;
import com.pda.retirements.service.RetirementService;
import com.pda.utils.api_utils.ApiUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.pda.utils.api_utils.ApiUtils.success;

@RestController
@Slf4j
@RequestMapping("/api/retirements")
@AllArgsConstructor
public class RetirementController {

    private RetirementService retirementService;

    @PostMapping("/test/results")
    public ApiUtils.ApiResult getTestResults(@RequestBody RetirementTestRequestDto testRequestDto) {
        // TODO: 토큰에서 유저 조회해서 나이, 성별 알기

        // 점수 계산
        int score = retirementService.getScore(testRequestDto);

        // 유형 판단
        RetirementType retirementType = retirementService.getRetirementType(score);
        RetirementTestResponseDto testResponseDto = new RetirementTestResponseDto(retirementType);
        return success(testResponseDto);
    }


}
