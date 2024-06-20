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
    public List<MydataInfoDto> mydataLink(UserAccountInfoDto userAccountInfoDto) {


        // controller에 { "userId": 1,"bankAccounts": ["국민은행", "신한은행"],"securityAccounts": ["신한투자증권"]} 형식으로 요청 온다
        // service에서는 각 기관을 돌면서 데이터 있는 지 확인
        // 있으면 mydataInfo에 있다고 저장
        // responseDto에 담아줌....

        int userId = userAccountInfoDto.getUserId();
        log.info("userId = {}", userId);

        List<MydataInfoDto> bankAccountsLinkInfo = bankAccountService.mydataLink(userId,  userAccountInfoDto.getBankAccounts());
        log.info("bankAccountsLinkInfo = {}", bankAccountsLinkInfo);



        return bankAccountsLinkInfo;
    }

}
