package com.pda.portfolio_service.service;

import com.pda.portfolio_service.dto.UserAgeTestScoreDto;
import com.pda.portfolio_service.feign.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DataService {

    private final MydataAssetClient mydataAssetClient;
    private final MydataUserClient mydataUserClient;
    public MydataDto getUserMydataByOpenFeign(int userId) {
        List<SecurityAccountStocksDto> securityStocks = mydataAssetClient.getSecurityStocks(userId).orElse(null);
        int totalAssets = mydataAssetClient.getTotalAssets2(userId);
        List<SecurityAccountTransactionsDto> securityTransactions = mydataAssetClient.getSecurityTransactions(userId).orElse(null);
        UserAgeTestScoreDto userAgeTestScoreDto = mydataUserClient.getAgeTestScoreByUserId(userId);

        return MydataDto.builder()
                .securityStocks(securityStocks)
                .securityTransactions(securityTransactions).totalAssets(totalAssets)
                .user(userAgeTestScoreDto).build();
    }
}
