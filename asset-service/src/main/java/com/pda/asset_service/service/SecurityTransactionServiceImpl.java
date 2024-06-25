package com.pda.asset_service.service;


import com.pda.asset_service.dto.SecurityAccountStocksDto;
import com.pda.asset_service.dto.SecurityAccountTransactionsDto;
import com.pda.asset_service.dto.SecurityTransactionDto;
import com.pda.asset_service.dto.SecurityTransactionResponseDto;
import com.pda.asset_service.jpa.MydataInfo;
import com.pda.asset_service.jpa.MydataInfoRepository;
import com.pda.asset_service.jpa.SecurityTransaction;
import com.pda.asset_service.jpa.SecurityTransactionRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class SecurityTransactionServiceImpl implements SecurityTransactionService{

    private final SecurityTransactionRepository securityTransactionRespository;
    private final MydataInfoRepository mydataInfoRepository;

    @Override
    public Optional<List<SecurityAccountTransactionsDto>> getSecurityTransactions(int userId) {
        Optional<List<MydataInfo>> securityAccounts = mydataInfoRepository.findByUserIdAndAssetType(userId, "security_accounts");
        if (securityAccounts.isPresent()) {
            List<SecurityAccountTransactionsDto> securityAccountTransactionsDtos = securityAccounts.get().stream()
                    .map(account -> {
                        String accountNo = account.getAccountNo();
                        Optional<List<SecurityTransaction>> securityTransactions = securityTransactionRespository.findAllByAccountNo(accountNo);
                        List<SecurityTransactionDto> securityTransactionDtos = securityTransactions.orElse(List.of()).stream()
                                .map(transaction -> SecurityTransactionDto.builder()
                                        .id(transaction.getId())
                                        .txDatetime(transaction.getTxDatetime())
                                        .txType(transaction.getTxType())
                                        .txAmount(String.valueOf(transaction.getTxAmount()))
                                        .balAfterTx(transaction.getBalAfterTx())
                                        .txQty(transaction.getTxQty())
                                        .stockCode(transaction.getStockCode())
                                        .build())
                                .toList();
                        return SecurityAccountTransactionsDto.builder()
                                .accountNo(accountNo)
                                .securityAccountTransactions(securityTransactionDtos)
                                .build();
                    })
                    .toList();
            return Optional.of(securityAccountTransactionsDtos);
        } else {
            return Optional.empty();
        }
    }





}
