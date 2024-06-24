package com.pda.portfolio_service.dto_test;

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
public class MyDataTestDto {

    private String name;
    private String investmentType;
    private String summary;
    private int minimumSubscriptionFee;
    private int stockExposure;
    private double return3m;
    private List<FundDto> funds;


    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FundDto {

        private String fundCode;
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
        private List<HititPortfoliosFundsStocksAndBondsResponseDto.FundStockDto> fundStocks;
        private List<HititPortfoliosFundsStocksAndBondsResponseDto.FundBondDto> fundBonds;


//        @Getter
//        @Setter
//        @AllArgsConstructor
//        @NoArgsConstructor
//        @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
//        public static class FundStockDto {
//
//            private String stockName;
//            private String size;
//            private String style;
//            private double weight;
//        }
//
//        @Getter
//        @Setter
//        @AllArgsConstructor
//        @NoArgsConstructor
//        @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
//        public static class FundBondDto {
//
//            private String bondName;
//            private String expiredDate;
//            private double duration;
//            private String credit;
//            private double weight;
//        }
    }
}
