package com.pda.portfolio_service.service;

import com.pda.portfolio_service.dto.HititResponseDto;
import com.pda.portfolio_service.jpa.Portfolio;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import com.pda.portfolio_service.jpa.PortfolioRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PortfolioService {
    @Autowired
    private PortfolioRepository portfolioRepository;

    public List<HititResponseDto> getHititPortfolios() {
        List<Portfolio> portfolios = portfolioRepository.findAll();

        return portfolios.stream()
                .map(portfolio -> new HititResponseDto(
                        portfolio.getId(),
                        portfolio.getName(),
                        portfolio.getInvestmentType(),
                        portfolio.getSummary(),
                        portfolio.getMinimumSubscriptionFee(),
                        portfolio.getStockExposure()))
                .collect(Collectors.toList());
    }
}