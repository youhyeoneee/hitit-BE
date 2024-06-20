package com.pda.investment_test.jpa.user_answer;

import com.pda.user_service.jpa.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserAnswerRepository extends JpaRepository<UserAnswer, Integer> {
    Optional<UserAnswer> findByUserId(int userId);
}
