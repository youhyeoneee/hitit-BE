package com.pda.portfolio_service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;
import java.util.Optional;

@FeignClient(name = "asset-service", url = "${service.url.asset}/api/assets")
public interface MydataAssetClient {

    // 보유 주식
    @GetMapping("/security-stocks/{userId}")
    Optional<List<SecurityAccountStocksDto>> getSecurityStocks(@PathVariable("userId") int userId);
    // 총 자산
    @GetMapping("/totalAssets/{userId}")
    Integer getTotalAssets2(@PathVariable("userId") int userId);

    // 거래내역
    @GetMapping("/security-transactions/{userId}")
    Optional<List<SecurityAccountTransactionsDto>> getSecurityTransactions(@PathVariable("userId") int userId);
}
