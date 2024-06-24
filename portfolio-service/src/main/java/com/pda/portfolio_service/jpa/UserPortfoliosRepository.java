package com.pda.portfolio_service.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserPortfoliosRepository extends JpaRepository<UserPortfolios, Integer> {
    Optional<UserPortfolios> findByUserId(Integer userId);
    boolean existsByUserId(Integer userId);
}
