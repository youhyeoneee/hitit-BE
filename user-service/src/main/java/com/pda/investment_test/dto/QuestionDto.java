package com.pda.investment_test.dto;

import com.pda.investment_test.jpa.answer.Answer;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;


@Getter
@AllArgsConstructor
public class QuestionDto {
    private String question;
    private List<Answer> answers;
}
