package com.pda.portfolio_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class HititPortfoliosResponseDto {
    private int id;
    private String name;
    private String investmentType;
    private String summary;
    private Integer minimumSubscriptionFee;
    private Integer stockExposure;
    private Float return3m;
}

