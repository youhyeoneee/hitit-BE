package com.pda.asset_service.service;


import com.pda.asset_service.dto.MydataInfoDto;
import com.pda.asset_service.dto.SecurityAccountDto;
import com.pda.asset_service.dto.SecurityAccountResponseDto;
import com.pda.asset_service.jpa.SecurityAccount;
import com.pda.asset_service.jpa.SecurityStock;
import com.pda.asset_service.jpa.SecurityTransaction;

import java.util.List;

public interface SecurityAccountService {
    SecurityAccount convertToEntity(SecurityAccountResponseDto securityAccountResponseDto);

    SecurityAccountDto convertToDto(SecurityAccount securityAccount);

    List<MydataInfoDto> linkMyDataAccount(int userId, List<String> securityAccounts);

    List<SecurityAccountDto> getSecurityAccounts(int userId);

    Integer getSecurityAccountsBalance(int userId);

    SecurityAccountDto getSecurityAccountShinhanDC(int userId);


}
