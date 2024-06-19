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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Comparator;

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
                .map(portfolio -> {
                    // 각 포트폴리오의 id 값으로 해당 포트폴리오의 펀드 데이터를 가져옴
                    List<PortfolioFund> fundProducts = portfolioFundRepository.findByIdPortfolioId(portfolio.getId());

                    double totalReturn = fundProducts.stream()
                            .mapToDouble(fund -> fund.getWeight() * fund.getReturn3m())
                            .sum() / 100;

                    float totalReturnFloat = BigDecimal.valueOf(totalReturn)
                            .setScale(2, RoundingMode.HALF_UP)
                            .floatValue();

                    return new HititPortfoliosResponseDto(
                            portfolio.getId(),
                            portfolio.getName(),
                            portfolio.getInvestmentType(),
                            portfolio.getSummary(),
                            portfolio.getMinimumSubscriptionFee(),
                            portfolio.getStockExposure(),
                            totalReturnFloat
                    );
                })
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
                .sorted(Comparator.comparingDouble(HititPortfoliosFundsResponseDto::getWeight).reversed())
                .collect(Collectors.toList());
    }
}