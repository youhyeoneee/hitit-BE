package com.pda.asset_service.controller;

import com.pda.asset_service.dto.*;
import com.pda.asset_service.jpa.SecurityAccount;
import com.pda.asset_service.service.*;
import com.pda.security.JwtTokenProvider;
import com.pda.utils.api_utils.ApiUtils;
import com.pda.utils.api_utils.CustomStringUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/assets")
@AllArgsConstructor
public class AssetController {

    private final JwtTokenProvider jwtTokenProvider;

    private final AssetServiceImpl assetService;
    private final BankAccountServiceImpl bankAccountService;
    private final SecurityAccountServiceImpl securityAccountService;
    private final CardServiceImpl cardService;
    private final PensionServiceImpl pensionService;
    private final LoanServiceImpl loanService;

    @PostMapping("/mydata-link")
    public ApiUtils.ApiResult<List<MydataInfoDto>> linkMydata(@RequestHeader("Authorization") String bearerToken,@RequestBody UserAccountInfoDto userAccountInfoDto){
        String token = CustomStringUtils.getToken(bearerToken);
        int userId = Integer.parseInt(jwtTokenProvider.getUsername(token));
        log.info("user id : " + userId);

        List<MydataInfoDto> bankAccountsLinkInfo = assetService.linkMydata(userId, userAccountInfoDto);
        return ApiUtils.success(bankAccountsLinkInfo);
    }

    @GetMapping("/bank-accounts")
    public ApiUtils.ApiResult<List<BankAccountDto>> getBankAccounts(@RequestHeader("Authorization") String bearerToken){
        String token = CustomStringUtils.getToken(bearerToken);
        int userId = Integer.parseInt(jwtTokenProvider.getUsername(token));
        log.info("user id : " + userId);

        List<BankAccountDto> bankAccounts = bankAccountService.getBankAccounts(userId);
        return ApiUtils.success(bankAccounts);
    }

    @GetMapping("/security-accounts")
    public ApiUtils.ApiResult<List<SecurityAccountDto>> getSecurityAccounts(@RequestHeader("Authorization") String bearerToken){
        String token = CustomStringUtils.getToken(bearerToken);
        int userId = Integer.parseInt(jwtTokenProvider.getUsername(token));
        log.info("user id : " + userId);

        List<SecurityAccountDto> securityAccounts = securityAccountService.getSecurityAccounts(userId);
        return ApiUtils.success(securityAccounts);
    }

    @GetMapping("/cards")
    public ApiUtils.ApiResult<List<CardDto>> getCards(@RequestHeader("Authorization") String bearerToken){
        String token = CustomStringUtils.getToken(bearerToken);
        int userId = Integer.parseInt(jwtTokenProvider.getUsername(token));
        log.info("user id : " + userId);

        List<CardDto> cards = cardService.getCards(userId);
        return ApiUtils.success(cards);
    }

    @GetMapping("/pensions")
    public ApiUtils.ApiResult<List<PensionDto>> getPensions(@RequestHeader("Authorization") String bearerToken){
        String token = CustomStringUtils.getToken(bearerToken);
        int userId = Integer.parseInt(jwtTokenProvider.getUsername(token));
        log.info("user id : " + userId);

        List<PensionDto>  pensions = pensionService.getPensions(userId);
        return ApiUtils.success(pensions);
    }

    @GetMapping("/loans")
    public ApiUtils.ApiResult<List<LoanDto>> getLoans(@RequestHeader("Authorization") String bearerToken){
        String token = CustomStringUtils.getToken(bearerToken);
        int userId = Integer.parseInt(jwtTokenProvider.getUsername(token));
        log.info("user id : " + userId);

        List<LoanDto> loans = loanService.getLoans(userId);
        return ApiUtils.success(loans);
    }

    @GetMapping("/totalAssets")
    public ApiUtils.ApiResult<Integer> getTotalAssets(@RequestHeader("Authorization") String bearerToken){
        String token = CustomStringUtils.getToken(bearerToken);
        int userId = Integer.parseInt(jwtTokenProvider.getUsername(token));
        log.info("user id : " + userId);

        Integer totalAssets = assetService.getTotalAssets(userId);
        log.info("totalAssets = {}", totalAssets);
        return ApiUtils.success(totalAssets);
    }
}
