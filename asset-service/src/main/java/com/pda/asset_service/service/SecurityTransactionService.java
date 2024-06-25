package com.pda.asset_service.service;


import com.pda.asset_service.dto.SecurityAccountTransactionsDto;
import com.pda.asset_service.dto.SecurityTransactionResponseDto;
import com.pda.asset_service.jpa.SecurityTransactionRepository;

import java.util.List;
import java.util.Optional;

public interface SecurityTransactionService {

    Optional<List<SecurityAccountTransactionsDto>> getSecurityTransactions(int userId);
}
