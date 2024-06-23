package com.pda.asset_service.feign;

import com.pda.asset_service.dto.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@FeignClient(name = "mydata-service", url = "http://localhost:8082")
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

    @GetMapping("/api/mydata/pension/unclaimed-retirement-accounts/{userId}")
    Optional<List<PensionResponseDto>> findByUserIdAndRetirementPensionClaimed(int userId);
}
