package com.pda.investment_test.service;


import com.pda.investment_test.jpa.InvestmentType;
import com.pda.investment_test.jpa.answer.Answer;
import com.pda.investment_test.jpa.answer.AnswerRepository;
import com.pda.investment_test.jpa.question.Question;
import com.pda.investment_test.jpa.question.QuestionRepository;
import com.pda.utils.exception.investment_tests.NotFoundQnAException;
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


    public int getScore(int questionNo, int answerNo) {
        Answer answer = answerRepository.findByQuestionNoAndNo(questionNo, answerNo)
                .orElseThrow(() -> new NotFoundQnAException(questionNo, answerNo));
        return answer.getScore();
    }

    public InvestmentType getInvestmentType(int score) {
        if (score <= 8) {
            return InvestmentType.CONSERVATIVE;
        } else if (score <= 16) {
            return InvestmentType.MODERATELY_CONSERVATIVE;
        } else if (score <= 24) {
            return InvestmentType.NEUTRAL;
        } else if (score <= 32) {
            return InvestmentType.AGGRESSIVE;
        } else {
            return InvestmentType.VERY_AGGRESSIVE;
        }
    }
}
