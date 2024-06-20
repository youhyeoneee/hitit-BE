package com.pda.mydata_service.controller;

import com.pda.mydata_service.dto.BankAccountDto;
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
    private final BankAccountServiceImpl bankAccountService;

//    @GetMapping("/bank-account/")
//    public ApiUtils.ApiResult<Optional<List<BankAccountDto>>> getBankAccountData(@RequestParam int userId ){
//        return ApiUtils.success(mydataService.getAllBankAccounts(userId));
//    }
    @GetMapping("/bank-account/{userId}/{bankName}")
    public Optional<List<BankAccountDto>> getBankAccountsByUserIdAndBankName(
            @PathVariable("userId") int userId,
            @PathVariable("bankName") String bankName) {

        log.info("mydata controller");

        Optional<List<BankAccountDto>> bankAccounts = mydataService.getBankAccountsByUserIdAndBankName(userId, bankName);
        log.info("mydata controller result = {}", bankAccounts);
        return bankAccounts;
    }
}
