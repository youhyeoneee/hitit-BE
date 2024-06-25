package com.pda.portfolio_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PortfolioFundAssetResponseDto {
    private float stock;
    private float stockForeign;
    private float bond;
    private float bondForeign;
    private float investment;
    private float etc;
}
