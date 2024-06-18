package com.pda.investment_test.jpa.answer;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Integer> {
    List<Answer> findByQuestionNo(int questionNo);
}
