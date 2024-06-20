package com.pda.investment_test.jpa.answer;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AnswerRepository extends JpaRepository<Answer, Integer> {
    List<Answer> findByQuestionNo(int questionNo);

    Optional<Answer> findByQuestionNoAndNo(int questionNo, int no);
}
