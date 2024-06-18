package com.pda.portfolio_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class HititPortfoliosFundsResponseDto {
    private String fund_code;
    private Integer portfolio_id;
    private String fund_name;
    private String fund_type_detail;
    private String company_name;
    private Float weight;
}
