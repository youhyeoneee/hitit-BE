package com.pda.portfolio_service.service;

import com.pda.portfolio_service.dto.HititPortfoliosFundsResponseDto;
import com.pda.portfolio_service.dto.HititPortfoliosResponseDto;
import com.pda.portfolio_service.jpa.Portfolio;
import com.pda.portfolio_service.jpa.PortfolioFund;
import com.pda.portfolio_service.jpa.PortfolioFundRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.pda.portfolio_service.jpa.PortfolioRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PortfolioService {
    @Autowired
    private PortfolioRepository portfolioRepository;

    @Autowired
    private PortfolioFundRepository portfolioFundRepository;

    public List<HititPortfoliosResponseDto> getHititPortfolios() {
        List<Portfolio> portfolios = portfolioRepository.findAll();

        return portfolios.stream()
                .map(portfolio -> new HititPortfoliosResponseDto(
                        portfolio.getId(),
                        portfolio.getName(),
                        portfolio.getInvestmentType(),
                        portfolio.getSummary(),
                        portfolio.getMinimumSubscriptionFee(),
                        portfolio.getStockExposure()))
                .collect(Collectors.toList());
    }

    public List<HititPortfoliosFundsResponseDto> getHititPortfoliosFunds(Integer portfolioId) {
        List<PortfolioFund> fundProducts = portfolioFundRepository.findByIdPortfolioId(portfolioId);

        return fundProducts.stream()
                .map(portfolioFund -> new HititPortfoliosFundsResponseDto(
                        portfolioFund.getId().getFundCode(),
                        portfolioFund.getId().getPortfolioId(),
                        portfolioFund.getFundName(),
                        portfolioFund.getFundTypeDetail(),
                        portfolioFund.getCompanyName(),
                        portfolioFund.getWeight(),
                        portfolioFund.getReturn3m()))
                .collect(Collectors.toList());
    }
}