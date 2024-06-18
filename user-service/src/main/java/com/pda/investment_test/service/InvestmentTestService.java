package com.pda.investment_test.service;


import com.pda.investment_test.jpa.answer.Answer;
import com.pda.investment_test.jpa.answer.AnswerRepository;
import com.pda.investment_test.jpa.question.Question;
import com.pda.investment_test.jpa.question.QuestionRepository;
import com.pda.utils.exception.investment_tests.NotFoundQuestionException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class InvestmentTestService {
    private QuestionRepository questionRepository;
    private AnswerRepository answerRepository;

    public String getQuestionById(int questionNo) throws NotFoundQuestionException {
        Question question = questionRepository.findByNo(questionNo)
                .orElseThrow(() -> new NotFoundQuestionException(questionNo));

        return question.getContent();
    }

    public List<Answer> getAnswersByQuestionId(int questionNo) {
        return answerRepository.findByQuestionNo(questionNo);
    }
}
