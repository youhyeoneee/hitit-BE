
package com.pda.portfolio_service.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FundBondsRepository extends JpaRepository<FundBonds, PortfolioFundBondId> {
    Optional<List<FundBonds>> findByIdFundCode(String fundCode);
}
