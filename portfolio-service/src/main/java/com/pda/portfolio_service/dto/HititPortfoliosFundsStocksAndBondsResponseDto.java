package com.pda.portfolio_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.Date;

@Getter
@AllArgsConstructor
public class HititPortfoliosFundsStocksAndBondsResponseDto {
    private String fundCode;
    private Integer portfolioId;
    private String fundName;
    private String fundTypeDetail;
    private String companyName;
    private double weight;
    private double return3m;

    // PortfolioFundAsset 관련 필드
    private Float stock;
    private Float stockForeign;
    private Float bond;
    private Float bondForeign;
    private Float investment;
    private Float etc;


    private List<FundStockDto> fundStocks;
    private List<FundBondDto> fundBonds;

    @Getter
    @AllArgsConstructor
    public static class FundStockDto {
        private String stock_name;
        private String size;
        private String style;
        private Float weight;
    }

    @Getter
    @AllArgsConstructor
    public static class FundBondDto {
        private String bond_name;
        private Date expiredDate;
        private Float duration;
        private String credit;
        private Float weight;
    }
}
