package com.pda.asset_service.controller;

import com.pda.asset_service.dto.*;
import com.pda.asset_service.jpa.SecurityAccount;
import com.pda.asset_service.service.*;
import com.pda.utils.api_utils.ApiUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/assets")
@AllArgsConstructor
public class AssetController {

    private final AssetServiceImpl assetService;
    private final BankAccountServiceImpl bankAccountService;
    private final SecurityAccountServiceImpl securityAccountService;
    private final CardServiceImpl cardService;
    private final PensionServiceImpl pensionService;
    private final LoanServiceImpl loanService;

    @PostMapping("/mydata-link")
    public ApiUtils.ApiResult<List<MydataInfoDto>> linkMydata(@RequestBody UserAccountInfoDto userAccountInfoDto){

        List<MydataInfoDto> bankAccountsLinkInfo = assetService.linkMydata(userAccountInfoDto);
        return ApiUtils.success(bankAccountsLinkInfo);
    }

    @GetMapping("/bank-accounts")
    public ApiUtils.ApiResult<List<BankAccountDto>> getBankAccounts(@RequestParam int userId){
        List<BankAccountDto> bankAccounts = bankAccountService.getBankAccounts(userId);
        return ApiUtils.success(bankAccounts);
    }

    @GetMapping("/security-accounts")
    public ApiUtils.ApiResult<List<SecurityAccountDto>> getSecurityAccounts(@RequestParam int userId){
        List<SecurityAccountDto> securityAccounts = securityAccountService.getSecurityAccounts(userId);
        return ApiUtils.success(securityAccounts);
    }

    @GetMapping("/cards")
    public ApiUtils.ApiResult<List<CardDto>> getCards(@RequestParam int userId){

        List<CardDto> cards = cardService.getCards(userId);
        return ApiUtils.success(cards);
    }

    @GetMapping("/pensions")
    public ApiUtils.ApiResult<List<PensionDto>> getPensions(@RequestParam int userId){

        List<PensionDto>  pensions = pensionService.getPensions(userId);
        return ApiUtils.success(pensions);
    }

    @GetMapping("/loans")
    public ApiUtils.ApiResult<List<LoanDto>> getLoans(@RequestParam int userId){
        List<LoanDto> loans = loanService.getLoans(userId);
        return ApiUtils.success(loans);
    }
}
