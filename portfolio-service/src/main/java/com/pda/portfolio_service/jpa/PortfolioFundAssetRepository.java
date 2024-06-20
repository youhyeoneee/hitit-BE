package com.pda.portfolio_service.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PortfolioFundAssetRepository extends JpaRepository<PortfolioFundAsset, String> {
    PortfolioFundAsset findByFundCode(String fundCode);
}