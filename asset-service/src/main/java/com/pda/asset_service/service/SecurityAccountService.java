package com.pda.asset_service.service;

import com.pda.asset_service.dto.*;
import com.pda.asset_service.jpa.BankAccount;
import com.pda.asset_service.jpa.SecurityAccount;

import java.util.List;

public interface SecurityAccountService {
    SecurityAccount convertToEntity(SecurityAccountResponseDto securityAccountResponseDto);

    SecurityAccountDto convertToDto(SecurityAccount securityAccount);

    List<MydataInfoDto> linkMyDataAccount(int userId, List<String> securityAccounts);

    List<SecurityAccountDto> getSecurityAccounts(int userId);
}
