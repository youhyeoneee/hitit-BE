package com.pda.portfolio_service.jpa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserPortfoliosFundProductsRepository extends JpaRepository<UserPortfoliosFundProducts, PortfolioFundId> {
    List<UserPortfoliosFundProducts> findByIdPortfolioId(Integer portfolioId);

    UserPortfoliosFundProducts findByIdPortfolioIdAndIdFundCode(Integer portfolioId, String fundCode);

    void deleteByIdPortfolioId(Integer id);
}
