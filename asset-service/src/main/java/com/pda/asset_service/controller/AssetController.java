package com.pda.asset_service.controller;

import com.pda.asset_service.dto.*;
import com.pda.asset_service.service.*;
//import com.pda.user_service.security.JwtTokenProvider;
import com.pda.utils.api_utils.ApiUtils;
import com.pda.utils.api_utils.CustomStringUtils;
import com.pda.utils.auth.AuthClient;
import com.pda.utils.auth.dto.AuthUserDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
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
    private final AuthClient authClient;

    @PostMapping("/mydata-link")
    public ApiUtils.ApiResult<List<MydataInfoDto>> linkMydata(@RequestHeader("Authorization") String bearerToken,
                                                              @RequestBody UserAccountInfoDto userAccountInfoDto) {

        AuthUserDto authUserDto = authClient.validateToken(bearerToken);
        int userId = authUserDto.getId();
        log.info("user id : " + userId);

        List<MydataInfoDto> bankAccountsLinkInfo = assetService.linkMydata(userId, userAccountInfoDto);
        return ApiUtils.success(bankAccountsLinkInfo);
    }

    @GetMapping("/bank-accounts")
    public ApiUtils.ApiResult<List<BankAccountDto>> getBankAccounts(@RequestHeader("Authorization") String bearerToken) {
        AuthUserDto authUserDto = authClient.validateToken(bearerToken);
        int userId = authUserDto.getId();
        log.info("user id : " + userId);
        List<BankAccountDto> bankAccounts = bankAccountService.getBankAccounts(userId);
        return ApiUtils.success(bankAccounts);
    }

    @GetMapping("/security-accounts")
    public ApiUtils.ApiResult<List<SecurityAccountDto>> getSecurityAccounts(@RequestParam("user_id") int userId) {
// TODO: 변경
//        String token = CustomStringUtils.getToken(bearerToken);
//        int userId = Integer.parseInt(jwtTokenProvider.getUsername(token));
        log.info("user id : " + userId);

        List<SecurityAccountDto> securityAccounts = securityAccountService.getSecurityAccounts(userId);
        return ApiUtils.success(securityAccounts);
    }

    @GetMapping("/cards")
    public ApiUtils.ApiResult<List<CardDto>> getCards(@RequestParam("user_id") int userId) {

//        TODO: 변경
//        String token = CustomStringUtils.getToken(bearerToken);
//        int userId = Integer.parseInt(jwtTokenProvider.getUsername(token));
        log.info("user id : " + userId);

        List<CardDto> cards = cardService.getCards(userId);
        return ApiUtils.success(cards);
    }

    @GetMapping("/pensions")
    public ApiUtils.ApiResult<List<PensionDto>> getPensions(@RequestParam("user_id") int userId) {
//        TODO: 변경
//        String token = CustomStringUtils.getToken(bearerToken);
//        int userId = Integer.parseInt(jwtTokenProvider.getUsername(token));
        log.info("user id : " + userId);

        List<PensionDto> pensions = pensionService.getPensions(userId);
        return ApiUtils.success(pensions);
    }

    @GetMapping("/loans")
    public ApiUtils.ApiResult<List<LoanDto>> getLoans(@RequestParam("user_id") int userId) {
//        TODO: 변경
//        String token = CustomStringUtils.getToken(bearerToken);
//        int userId = Integer.parseInt(jwtTokenProvider.getUsername(token));
        log.info("user id : " + userId);

        List<LoanDto> loans = loanService.getLoans(userId);
        return ApiUtils.success(loans);
    }

    @GetMapping("/totalAssets")
    public ApiUtils.ApiResult<Integer> getTotalAssets(@RequestParam("user_id") int userId) {
//        TODO: 변경
//        String token = CustomStringUtils.getToken(bearerToken);
//        int userId = Integer.parseInt(jwtTokenProvider.getUsername(token));
        log.info("user id : " + userId);

        Integer totalAssets = assetService.getTotalAssets(userId);
        log.info("totalAssets = {}", totalAssets);
        return ApiUtils.success(totalAssets);
    }
}