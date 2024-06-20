package com.pda.investment_test.service;


import com.pda.investment_test.jpa.InvestmentType;
import com.pda.investment_test.jpa.answer.Answer;
import com.pda.investment_test.jpa.answer.AnswerRepository;
import com.pda.investment_test.jpa.question.Question;
import com.pda.investment_test.jpa.question.QuestionRepository;
import com.pda.investment_test.jpa.user_answer.UserAnswer;
import com.pda.investment_test.jpa.user_answer.UserAnswerRepository;
import com.pda.user_service.jpa.User;
import com.pda.user_service.jpa.UserRepository;
import com.pda.utils.exception.investment_tests.AnswerNotFoundException;
import com.pda.utils.exception.investment_tests.QuestionNotFoundException;
import com.pda.utils.exception.investment_tests.UserAnswerNotFoundException;
import com.pda.utils.exception.login.NotFoundUserException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class InvestmentTestService {
    private QuestionRepository questionRepository;
    private AnswerRepository answerRepository;
    private UserAnswerRepository userAnswerRepository;
    private UserRepository userRepository;

    public String getQuestionById(int questionNo) {
        Question question = questionRepository.findByNo(questionNo)
                .orElseThrow(() -> new QuestionNotFoundException(questionNo));

        return question.getContent();
    }

    public List<Answer> getAnswersByQuestionId(int questionNo) {
        return answerRepository.findByQuestionNo(questionNo);
    }


    public int getScore(int questionNo, int answerNo) {
        Answer answer = answerRepository.findByQuestionNoAndNo(questionNo, answerNo)
                .orElseThrow(() -> new AnswerNotFoundException(questionNo, answerNo));
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

    public String getAnswerContent(int questionNo, int answerNo)  {
        Answer answer = answerRepository.findByQuestionNoAndNo(questionNo, answerNo)
                .orElseThrow(() -> new AnswerNotFoundException(questionNo, answerNo));

        return answer.getContent();
    }

    public UserAnswer saveUserAnswers(int userId, List<String> answers) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundUserException(userId + "번 유저를 찾을 수 없습니다."));

        Optional<UserAnswer> savedUserAnswer = userAnswerRepository.findByUserId(userId);

        if (savedUserAnswer.isEmpty()) {
            UserAnswer userAnswer = new UserAnswer(userId, answers);
            userAnswerRepository.save(userAnswer);

            return userAnswer;
        } else {
            UserAnswer userAnswer = savedUserAnswer.get();
            userAnswer.setAnswers(answers);
            userAnswer.setUpdatedAt(LocalDateTime.now());
            userAnswerRepository.save(userAnswer);

            return userAnswer;
        }
    }

    public List<String> getResultsByUserId(int userId) {
        UserAnswer userAnswer = userAnswerRepository.findByUserId(userId)
                .orElseThrow(() -> new UserAnswerNotFoundException(userId + "번 유저의 저장된 테스트 결과가 없습니다."));

        return userAnswer.getAnswers();
    }
}
