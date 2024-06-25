package com.pda.asset_service.feign;

import com.pda.asset_service.dto.*;
import com.pda.asset_service.jpa.SecurityStock;
import com.pda.asset_service.jpa.SecurityTransaction;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@PropertySource(value = "env.properties")
@FeignClient(name = "mydata-service", url = "${service.url.mydata}")
public interface MydataServiceClient {

    @GetMapping("/api/mydata/bank-account/{userId}")
    List<BankAccountDto> getAllBankAccountData(@PathVariable("userId") int userId);
    @GetMapping("/api/mydata/bank-account/{userId}/{bankName}")
    Optional<List<BankAccountResponseDto>> getBankAccountsByUserIdAndBankName(@PathVariable("userId") int userId, @PathVariable("bankName") String bankName);

    @GetMapping("/api/mydata/loan/{userId}/{companyName}")
    Optional<List<LoanResponseDto>> getLoansByUserIdAndCompanyName(@PathVariable("userId") int userId, @PathVariable("companyName") String companyName);

    @GetMapping("/api/mydata/card/{userId}/{companyName}")
    Optional<List<CardResponseDto>> getCardsByUserIdAndCompanyName(@PathVariable("userId") int userId, @PathVariable("companyName") String companyName);

    @GetMapping("/api/mydata/pension/{userId}/{companyName}")
    Optional<List<PensionResponseDto>> getPensionsByUserIdAndCompanyName(@PathVariable("userId") int userId, @PathVariable("companyName") String companyName);

    @GetMapping("/api/mydata/security-account/{userId}/{securityName}")
    Optional<List<SecurityAccountResponseDto>> getSecurityAccountsByUserIdAndSecurityName(@PathVariable("userId") int userId, @PathVariable("securityName") String securityName);


    // 미청구 퇴직 연금
    @GetMapping("/api/mydata/pension/unclaimed-retirement-accounts/{userId}")
    Optional<List<RetirementAccountResponseDto>> getUnclaimedRetirementAccounts(@PathVariable("userId") int userId);

    // 증권 거래 내역
    @GetMapping("/api/mydata/security-transactions/{accountNo}")
    Optional<List<SecurityTransactionResponseDto>> getSecurityTransactions(@PathVariable("accountNo") String accountNo);

    // 보유 주식
    @GetMapping("/api/mydata/security-stocks/{accountNo}")
    Optional<List<SecurityStockResponseDto>> getSecurityStocks(@PathVariable("accountNo") String accountNo);

}
