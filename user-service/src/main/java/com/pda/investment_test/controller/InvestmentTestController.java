package com.pda.investment_test.controller;

import com.pda.investment_test.dto.QuestionDto;
import com.pda.investment_test.jpa.answer.Answer;
import com.pda.investment_test.service.InvestmentTestService;
import com.pda.utils.api_utils.ApiUtils;
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

    @GetMapping("/questions/{no}")
    public ApiUtils.ApiResult getQuestionById(@PathVariable int no) {
        String content = investmentTestService.getQuestionById(no);
        List<Answer> answers = investmentTestService.getAnswersByQuestionId(no);
        QuestionDto questionDto = new QuestionDto(content, answers);
        return success(questionDto);
    }
}
