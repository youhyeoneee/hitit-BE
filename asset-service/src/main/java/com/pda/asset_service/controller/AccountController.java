package com.pda.asset_service.controller;

import com.pda.asset_service.dto.AccountDto;
import com.pda.asset_service.dto.AccountCreateDto;
import com.pda.asset_service.dto.SecurityAccountDto;
import com.pda.asset_service.service.AccountServiceImpl;
import com.pda.asset_service.sms.SMSCertificationService;
import com.pda.asset_service.sms.UserDto;
import com.pda.security.JwtTokenProvider;
import com.pda.utils.api_utils.ApiUtils;
import com.pda.utils.api_utils.CustomStringUtils;
import com.pda.utils.exception.sms.SmsCertificationException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static com.pda.utils.api_utils.ApiUtils.error;
import static com.pda.utils.api_utils.ApiUtils.success;

@RestController
@Slf4j
@RequestMapping("/api/assets")
@AllArgsConstructor
public class AccountController {

    private final SMSCertificationService smsCertificationService;
    private final AccountServiceImpl accountService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/auth/send")
    public ApiUtils.ApiResult<String> sendSms(@RequestBody UserDto.SmsCertificationRequest requestDto) throws Exception {
        try {
            smsCertificationService.sendSms(requestDto);
            return success("인증 코드를 성공적으로 보냈습니다");
        } catch (Exception e) {
            return error("잘못된 요청", HttpStatus.BAD_REQUEST);
        }
    }

    //인증번호 확인
    @PostMapping("/auth/confirm")
    public ApiUtils.ApiResult<String> SmsVerification(@RequestBody UserDto.SmsCertificationRequest requestDto) throws Exception{
        try {
            smsCertificationService.verifySms(requestDto);
            return success("인증 코드가 일치합니다.");
        } catch (SmsCertificationException.Exception e) {
            return error("인증번호가 일치하지 않습니다.",HttpStatus.BAD_REQUEST);
        }
    }

    // DC 퇴직연금 신한투자증권 계좌가 있는지 조회
    @GetMapping("/account/shinhan")
    public ApiUtils.ApiResult<SecurityAccountDto> checkAccountShinhan(@RequestHeader("Authorization") String bearerToken){
        String token = CustomStringUtils.getToken(bearerToken);
        int userId = Integer.parseInt(jwtTokenProvider.getUsername(token));
        log.info("user id : " + userId);

        SecurityAccountDto securityAccountDto = accountService.getRetirementAccountShinhan(userId);
        return ApiUtils.success(securityAccountDto);
    }

//     계좌 개설
    @PostMapping("/account")
    public ApiUtils.ApiResult<AccountDto> createAccount(@RequestHeader("Authorization") String bearerToken, @RequestBody AccountCreateDto accountCreateDto){
        String token = CustomStringUtils.getToken(bearerToken);
        int userId = Integer.parseInt(jwtTokenProvider.getUsername(token));
        log.info("user id : " + userId);

        AccountDto accountDto = accountService.createAccount(userId);
        return ApiUtils.success(accountDto);
    }

//     내 계좌 조회
    @GetMapping("/account")
    public ApiUtils.ApiResult<AccountDto> getAccount(@RequestHeader("Authorization") String bearerToken){
        String token = CustomStringUtils.getToken(bearerToken);
        int userId = Integer.parseInt(jwtTokenProvider.getUsername(token));
        log.info("user id : " + userId);

        AccountDto accountDto = accountService.getAccount(userId);
        return ApiUtils.success(accountDto);
    }



}
