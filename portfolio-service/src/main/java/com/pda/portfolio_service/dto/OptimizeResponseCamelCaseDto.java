package com.pda.portfolio_service.dto;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class OptimizeResponseCamelCaseDto {
    private Response response;

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @ToString
    public static class Response {
        private List<Fund> funds;
        private Integer userId;
        private List<Float> weights;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @ToString
    public static class Fund {
        private String fundCode;
        private List<Stock> stocks;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @ToString
    public static class Stock {
        private String stockCode;
        private String stockName;
        private String badNewsTitle;
        private String badNewsUrl;
        private String rev;
        private String income;
    }
}
