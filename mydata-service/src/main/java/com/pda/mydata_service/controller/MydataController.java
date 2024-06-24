package com.pda.mydata_service.controller;

import com.pda.mydata_service.dto.*;
import com.pda.mydata_service.service.BankAccountServiceImpl;
import com.pda.mydata_service.service.MydataServiceImpl;
import com.pda.utils.api_utils.ApiUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;


@RestController
@Slf4j
@RequestMapping("/api/mydata")
@AllArgsConstructor
public class MydataController {

    private final MydataServiceImpl mydataService;


    @GetMapping("/bank-account/{userId}/{bankName}")
    public Optional<List<BankAccountDto>> getBankAccountsByUserIdAndBankName(
            @PathVariable("userId") int userId,
            @PathVariable("bankName") String bankName) {
        Optional<List<BankAccountDto>> bankAccounts = mydataService.getBankAccountsByUserIdAndBankName(userId, bankName);
        return bankAccounts;
    }

    @GetMapping("/loan/{userId}/{loanName}")
    public Optional<List<LoanDto>> getLoansByUserIdAndCompanyName(
            @PathVariable("userId") int userId,
            @PathVariable("loanName") String companyName) {
        Optional<List<LoanDto>> loans = mydataService.getLoansByUserIdAndCompanyName(userId, companyName);
        return loans;
    }

    @GetMapping("/card/{userId}/{cardName}")
    public Optional<List<CardDto>> getCardsByUserIdAndCompanyName(
            @PathVariable("userId") int userId,
            @PathVariable("cardName") String companyName) {
        Optional<List<CardDto>> cards = mydataService.getCardsByUserIdAndCompanyName(userId, companyName);
        return cards;
    }

    @GetMapping("/pension/{userId}/{pensionName}")
    public Optional<List<PensionDto>> getPensionsByUserIdAndCompanyName(
            @PathVariable("userId") int userId,
            @PathVariable("pensionName") String companyName) {
        Optional<List<PensionDto>> pensions = mydataService.getPensionsByUserIdAndCompanyName(userId, companyName);
        return pensions;
    }

    @GetMapping("/security-account/{userId}/{securityName}")
    public Optional<List<SecurityAccountDto>> getSecurityAccountsByUserIdAndSecurityName(
            @PathVariable("userId") int userId,
            @PathVariable("securityName") String securityName) {
        Optional<List<SecurityAccountDto>> securityAccounts = mydataService.getSecurityAccountsByUserIdAndSecurityName(userId, securityName);
        return securityAccounts;
    }

    @GetMapping("/pension/unclaimed-retirement-accounts/{userId}")
    public Optional<List<PensionDto>> getUnclaimedRetirementAccounts(@PathVariable("userId") int userId){
        Optional<List<PensionDto>> unclaimedRetirementAccounts = mydataService.getUnclaimedRetirementAccounts(userId);
        return unclaimedRetirementAccounts;
    }

    // 증권 거래 내역
    @GetMapping("/security-transactions/{accountNo}")
    public Optional<List<SecurityTransactionDto>> getSecurityTransactions(@PathVariable("accountNo") String accountNo){
        log.info("ACCOUNTNO + {}", accountNo);
        Optional<List<SecurityTransactionDto>> securityTransactions = mydataService.getSecurityTransactions(accountNo);
        return securityTransactions;
    }

    // 보유 주식
    @GetMapping("/security-stocks/{accountNo}")
    public Optional<List<SecurityStockDto>> getSecurityStocks(@PathVariable("accountNo") String accountNo){
        Optional<List<SecurityStockDto>> securityStocks = mydataService.getSecurityStocks(accountNo);
        return securityStocks;
    }
}