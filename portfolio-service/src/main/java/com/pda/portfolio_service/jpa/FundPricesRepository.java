package com.pda.portfolio_service.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface FundPricesRepository extends JpaRepository<FundPrices, FundPricesId> {

    List<FundPrices> findByIdFundCodeAndIdDateAfter(String fundCode, Date createdAt);
}
