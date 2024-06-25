package com.pda.portfolio_service.feign;

import com.pda.portfolio_service.dto.UserAgeTestScoreDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MydataDto {
    private List<SecurityAccountStocksDto> securityStocks;
    private Integer totalAssets;
    private List<SecurityAccountTransactionsDto> securityTransactions;
    private UserAgeTestScoreDto user;
}

