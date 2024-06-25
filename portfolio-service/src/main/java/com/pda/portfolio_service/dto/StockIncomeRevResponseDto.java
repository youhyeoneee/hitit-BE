package com.pda.portfolio_service.dto;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.pda.portfolio_service.feign.MyDataFlaskLevelTestResponseDto;
import lombok.*;

import java.util.List;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class StockIncomeRevResponseDto {
    private StockIncomeRevDto response;

    @ToString
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class StockIncomeRevDto {
        private String income;
        private String rev;
    }
}

