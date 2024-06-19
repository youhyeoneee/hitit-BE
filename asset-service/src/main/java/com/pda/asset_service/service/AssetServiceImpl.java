package com.pda.asset_service.service;

import com.pda.asset_service.dto.BankAccountDto;
import com.pda.asset_service.dto.MydataInfoDto;
import com.pda.asset_service.dto.UserAccountInfoDto;
import com.pda.asset_service.feign.MydataServiceClient;
import com.pda.asset_service.jpa.BankAccountRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class AssetServiceImpl implements AssetService{

    private final BankAccountRepository bankAccountRepository;
    private final MydataServiceClient mydataServiceClient;

    private final BankAccountServiceImpl bankAccountService;
    private final CardServiceImpl cardService;
    private final SecurityTransactionServiceImpl securityTransactionService;

    @Override
    public List<MydataInfoDto> linkMydata(UserAccountInfoDto userAccountInfoDto) {

        int userId = userAccountInfoDto.getUserId();
        log.info("userId = {}", userId);

        List<MydataInfoDto> bankAccountsLinkInfo = bankAccountService.linkMyDataAccount(userId,  userAccountInfoDto.getBankAccounts());
        log.info("bankAccountsLinkInfo = {}", bankAccountsLinkInfo);

        return bankAccountsLinkInfo;
    }

}
