package com.pda.asset_service.controller;

import com.pda.asset_service.dto.*;
import com.pda.asset_service.service.*;
import com.pda.utils.api_utils.ApiUtils;
import com.pda.utils.security.JwtTokenProvider;
import com.pda.utils.security.openfeign.AuthClient;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static com.pda.utils.api_utils.ApiUtils.success;

@RestController
@Slf4j
@RequestMapping("/api/assets")
@AllArgsConstructor
@PropertySource(value = {"env.properties"})
public class AssetController {

    private final AssetServiceImpl assetService;
    private final BankAccountServiceImpl bankAccountService;
    private final SecurityAccountServiceImpl securityAccountService;
    private final CardServiceImpl cardService;
    private final PensionServiceImpl pensionService;
    private final LoanServiceImpl loanService;
    private final AuthClient authClient;
    private final JwtTokenProvider jwtTokenProvider;
    private final SecurityStockServiceImpl securityStockService;
    private final SecurityTransactionServiceImpl securityTransactionService;

    @PostMapping("/mydata-link")
    public ApiUtils.ApiResult<List<MydataInfoDto>> linkMydata(@RequestHeader("Authorization") String bearerToken,
                                                              @RequestBody UserAccountInfoDto userAccountInfoDto) {

        int userId =  jwtTokenProvider.bearerToken2UserId(bearerToken);
        log.info("user id : " + userId);

        List<MydataInfoDto> bankAccountsLinkInfo = assetService.linkMydata(userId, userAccountInfoDto);
        return success(bankAccountsLinkInfo);
    }

    @GetMapping("/bank-accounts")
    public ApiUtils.ApiResult<List<BankAccountDto>> getBankAccounts(@RequestHeader("Authorization") String bearerToken) {
        int userId =  jwtTokenProvider.bearerToken2UserId(bearerToken);
        log.info("user id : " + userId);
        List<BankAccountDto> bankAccounts = bankAccountService.getBankAccounts(userId);
        return success(bankAccounts);
    }

    @GetMapping("/security-accounts")
    public ApiUtils.ApiResult<List<SecurityAccountDto>> getSecurityAccounts(@RequestHeader("Authorization") String bearerToken) {
        int userId = jwtTokenProvider.bearerToken2UserId(bearerToken);
        log.info("user id : " + userId);

        List<SecurityAccountDto> securityAccounts = securityAccountService.getSecurityAccounts(userId);
        return success(securityAccounts);
    }

    @GetMapping("/cards")
    public ApiUtils.ApiResult<List<CardDto>> getCards(@RequestHeader("Authorization") String bearerToken) {

        int userId = jwtTokenProvider.bearerToken2UserId(bearerToken);
        log.info("user id : " + userId);

        List<CardDto> cards = cardService.getCards(userId);
        return success(cards);
    }

    @GetMapping("/pensions")
    public ApiUtils.ApiResult<List<PensionDto>> getPensions(@RequestHeader("Authorization") String bearerToken) {
        int userId = jwtTokenProvider.bearerToken2UserId(bearerToken);
        log.info("user id : " + userId);

        List<PensionDto> pensions = pensionService.getPensions(userId);
        return ApiUtils.success(pensions);
    }

    @GetMapping("/loans")
    public ApiUtils.ApiResult<List<LoanDto>> getLoans(@RequestHeader("Authorization") String bearerToken) {
        int userId = jwtTokenProvider.bearerToken2UserId(bearerToken);
        log.info("user id : " + userId);

        List<LoanDto> loans = loanService.getLoans(userId);
        return success(loans);
    }

    @GetMapping("/totalAssets")
    public ApiUtils.ApiResult<Integer> getTotalAssets(@RequestHeader("Authorization") String bearerToken) {
        int userId = jwtTokenProvider.bearerToken2UserId(bearerToken);
        log.info("user id : " + userId);

        Integer totalAssets = assetService.getTotalAssets(userId);
        log.info("totalAssets = {}", totalAssets);
        return success(totalAssets);
    }

    @GetMapping("/retirement-pension-claim")
    public ApiUtils.ApiResult<List<RetirementAccountDto>> getUnclaimedRetirementAccounts(@RequestHeader("Authorization") String bearerToken){
        int userId = jwtTokenProvider.bearerToken2UserId(bearerToken);
        log.info("user id : " + userId);

        List<RetirementAccountDto> unclaimedRetirementAccounts = assetService.getUnclaimedRetirementAccounts(userId);
        log.info(unclaimedRetirementAccounts.toString());
        return success(unclaimedRetirementAccounts);
    }

    // portfolio openFeign mapping
    // 보유 주식
    @GetMapping("/security-stocks/{userId}")
    public Optional<List<SecurityAccountStocksDto>> getSecurityStocks(@PathVariable("userId") int userId){

        Optional<List<SecurityAccountStocksDto>> securityStockResponseDtos = securityStockService.getSecurityStocks(userId);
        return securityStockResponseDtos;
    }
    // 총자산
    @GetMapping("/totalAssets/{userId}")
    public Integer getTotalAssets2(@PathVariable("userId") int userId){
        Integer totalAssets = assetService.getTotalAssets(userId);
        return totalAssets;
    }

    // 거래내역
    @GetMapping("/security-transactions/{userId}")
    public Optional<List<SecurityAccountTransactionsDto>> getSecurityTransactions(@PathVariable("userId") int userId){
        Optional<List<SecurityAccountTransactionsDto>> securityTransactionResponseDtos = securityTransactionService.getSecurityTransactions(userId);
        return securityTransactionResponseDtos;
    }


}
