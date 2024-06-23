package com.pda.asset_service.service;

import com.pda.asset_service.dto.AccountDto;
import com.pda.asset_service.dto.SecurityAccountDto;

public interface AccountService {

    SecurityAccountDto getRetirementAccountShinhan(int userId);

    AccountDto createAccount(int userId);

    AccountDto getAccount(int userId);
}
