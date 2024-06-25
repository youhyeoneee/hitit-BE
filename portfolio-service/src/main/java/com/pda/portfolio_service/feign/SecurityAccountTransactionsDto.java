package com.pda.portfolio_service.feign;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SecurityAccountTransactionsDto {

    private String accountNo;

    private List<SecurityTransactionDto> securityAccountTransactions;
}
