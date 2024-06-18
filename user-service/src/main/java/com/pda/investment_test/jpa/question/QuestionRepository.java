package com.pda.investment_test.jpa.question;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface QuestionRepository extends JpaRepository<Question, Integer> {
    Optional<Question> findByNo(int no);
}
