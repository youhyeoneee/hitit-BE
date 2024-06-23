package com.pda.asset_service.service;

import com.pda.asset_service.dto.AccountDto;
import com.pda.asset_service.dto.PensionDto;
import com.pda.asset_service.dto.SecurityAccountDto;
import com.pda.asset_service.jpa.AccountRepository;
import com.pda.asset_service.jpa.SecurityAccount;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService{

//    private final AccountRepository accountRepository;
    private final SecurityAccountServiceImpl securityAccountService;


    @Override
    public SecurityAccountDto getRetirementAccountShinhan(int userId) {
        SecurityAccountDto securityAccount = securityAccountService.getSecurityAccountShinhanDC(userId);

        return securityAccount;
    }

    @Override
    public AccountDto createAccount(int userId) {
        return null;
    }

    @Override
    public AccountDto getAccount(int userId) {
        return null;
    }
}
