package com.pda.portfolio_service.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.pda.portfolio_service.dto.HititPortfoliosFundsStocksAndBondsResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MyDataPortfolioDto {
    private String name;
    private String investmentType;
    private String summary;
    private int minimumSubscriptionFee;
    private int stockExposure;
    private double return3m;
    private List<MyDataPortfolioFundDto> funds;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MyDataPortfolioFundDto {
        private String fundCode;
        private String fundName;
        private String fundTypeDetail;
        private String companyName;
        private Float weight;
        private Float return3m;
        private Float stock;
        private Float stockForeign;
        private Float bond;
        private Float bondForeign;
        private Float investment;
        private Float etc;
        private List<HititPortfoliosFundsStocksAndBondsResponseDto.FundStockDto> fundStocks;
        private List<HititPortfoliosFundsStocksAndBondsResponseDto.FundBondDto> fundBonds;
    }
}
