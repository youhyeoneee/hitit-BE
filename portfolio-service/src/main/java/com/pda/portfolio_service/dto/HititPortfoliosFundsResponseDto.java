package com.pda.portfolio_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class HititPortfoliosFundsResponseDto {
    private String fundCode;
    private Integer portfolioId;
    private String fundName;
    private String fundTypeDetail;
    private String companyName;
    private Float weight;
    private Float return3m;
}
