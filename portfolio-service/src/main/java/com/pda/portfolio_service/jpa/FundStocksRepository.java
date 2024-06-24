package com.pda.portfolio_service.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FundStocksRepository extends JpaRepository<FundStocks, PortfolioFundId> {
    Optional<List<FundStocks>> findByIdFundCode(String fundCode);
}
