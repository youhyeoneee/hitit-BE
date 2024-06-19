package com.pda.investment_test.controller;

import com.pda.investment_test.dto.QuestionDto;
import com.pda.investment_test.dto.ResultDto;
import com.pda.investment_test.jpa.answer.Answer;
import com.pda.investment_test.service.InvestmentTestService;
import com.pda.security.JwtTokenProvider;
import com.pda.user_service.service.UserService;
import com.pda.utils.api_utils.ApiUtils;
import com.pda.utils.api_utils.CustomStringUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.pda.utils.api_utils.ApiUtils.success;

@RestController
@Slf4j
@RequestMapping("/api/investment_tests")
@AllArgsConstructor
public class InvestmentTestController {

    private InvestmentTestService investmentTestService;
    private JwtTokenProvider jwtTokenProvider;
    private UserService userService;

    @GetMapping("/questions/{no}")
    public ApiUtils.ApiResult getQuestionById(@PathVariable int no) {
        String content = investmentTestService.getQuestionById(no);
        List<Answer> answers = investmentTestService.getAnswersByQuestionId(no);
        QuestionDto questionDto = new QuestionDto(content, answers);
        return success(questionDto);
    }

    @PostMapping("/results")
    public ApiUtils.ApiResult<String> getResults(@RequestBody List<ResultDto> resultDtos, @RequestHeader("Authorization") String bearerToken) {
        int total_score = 0;
        for(int i=0; i< resultDtos.size(); i++) {
            int question_no = resultDtos.get(i).getQuestion();
            int answer_no = resultDtos.get(i).getAnswer();
            int score = investmentTestService.getScore(question_no, answer_no);
            log.info(question_no + "번 질문 - " +  answer_no + "번 대답 : 점수 : " + score);
            total_score += score;
        }

        log.info("total_score : " + total_score);

        // 유저 DB에 타입 저장하기
        log.info("bearerToken : " + bearerToken);
        String token = CustomStringUtils.getToken(bearerToken);
        int userId = Integer.parseInt(jwtTokenProvider.getUsername(token));
        log.info("user id : " + userId);

        String investmentType = investmentTestService.getInvestmentType(total_score).getDescription();
        log.info("investmentType : " + investmentType);
        userService.updateInvestmentTypeAndScore(userId, investmentType, total_score);
        
        return success(investmentType);
    }
}

