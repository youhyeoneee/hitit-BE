package com.pda.portfolio_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class HititPortfoliosFundsResponseDto {
    private String fund_code;
    private String stock_name;
    private String size;
    private String style;
    private Float weight;
}
