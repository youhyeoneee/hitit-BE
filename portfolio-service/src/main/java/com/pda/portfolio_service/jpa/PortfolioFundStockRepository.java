package com.pda.portfolio_service.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PortfolioFundStockRepository extends JpaRepository<PortfolioFundStock, PortfolioFundId> {
    Optional<List<PortfolioFundStock>> findByIdFundCode(String fundCode);
}
