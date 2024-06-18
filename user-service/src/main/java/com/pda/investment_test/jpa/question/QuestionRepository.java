package com.pda.investment_test.jpa.question;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface QuestionRepository extends JpaRepository<Question, Integer> {
    Optional<Question> findByNo(int no);
}
