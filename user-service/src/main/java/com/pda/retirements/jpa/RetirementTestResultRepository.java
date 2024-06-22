package com.pda.retirements.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RetirementTestResultRepository extends JpaRepository<RetirementTestResult, Integer> {
    Optional<RetirementTestResult> findByUserId(int userId);
}
