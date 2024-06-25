package com.pda.asset_service.service;

import com.pda.asset_service.dto.SecurityAccountStocksDto;
import com.pda.asset_service.dto.SecurityStockResponseDto;
import com.pda.asset_service.dto.SecurityTransactionResponseDto;
import com.pda.asset_service.jpa.MydataInfo;
import com.pda.asset_service.jpa.MydataInfoRepository;
import com.pda.asset_service.jpa.SecurityStock;
import com.pda.asset_service.jpa.SecurityStockRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class SecurityStockServiceImpl implements SecurityStockService{

    private final SecurityStockRepository securityStockRepository;
    private final MydataInfoRepository mydataInfoRepository;

    @Override
    public Optional<List<SecurityAccountStocksDto>> getSecurityStocks(int userId) {
        Optional<List<MydataInfo>> securityAccounts = mydataInfoRepository.findByUserIdAndAssetType(userId, "security_accounts");
        log.info("사용자가 보유한 증권 계좌임니다~~~~");
        if (securityAccounts.isPresent()) {
            List<SecurityAccountStocksDto> securityAccountStocksDtos = securityAccounts.get().stream()
                    .map(account -> {
                        String accountNo = account.getAccountNo();
                        Optional<List<SecurityStock>> securityStocks = securityStockRepository.findAllByAccountNo(accountNo);
                        List<String> stockCodes = securityStocks.map(stocks -> stocks.stream()
                                        .map(SecurityStock::getStockCode)
                                        .toList())
                                .orElse(List.of());
                        return SecurityAccountStocksDto.builder()
                                .accountNo(accountNo)
                                .stockCodes(stockCodes)
                                .build();
                    })
                    .toList();
            return Optional.of(securityAccountStocksDtos);
        } else {
            return Optional.empty();
        }
    }

}
