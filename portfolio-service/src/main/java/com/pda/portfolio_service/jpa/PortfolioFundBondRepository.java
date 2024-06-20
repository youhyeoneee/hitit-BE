package com.pda.portfolio_service.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PortfolioFundBondRepository extends JpaRepository<PortfolioFundBond, PortfolioFundBondId> {
    Optional<List<PortfolioFundBond>> findByIdFundCode(String fundCode);
}
