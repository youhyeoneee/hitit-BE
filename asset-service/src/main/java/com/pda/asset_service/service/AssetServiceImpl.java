package com.pda.asset_service.service;



import com.pda.asset_service.dto.MydataInfoDto;
import com.pda.asset_service.dto.PensionDto;
import com.pda.asset_service.dto.UserAccountInfoDto;
import com.pda.asset_service.jpa.MydataInfoRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class AssetServiceImpl implements AssetService{

    private final BankAccountServiceImpl bankAccountService;
    private final SecurityAccountServiceImpl securityAccountService;
    private final CardServiceImpl cardService;
    private final PensionServiceImpl pensionService;
    private final LoanServiceImpl loanService;
//    private final FundServiceImpl fundService;

    private final MydataInfoRepository mydataInfoRepository;


    @Override
    @Transactional
    public List<MydataInfoDto> linkMydata(int userId, UserAccountInfoDto userAccountInfoDto) {



        List<MydataInfoDto> allMydataLinkInfo = new ArrayList<>();

        // 은행 계좌
        List<MydataInfoDto> bankAccountsLinkInfo = bankAccountService.linkMyDataAccount(userId,userAccountInfoDto.getBankAccounts());
        allMydataLinkInfo.addAll(bankAccountsLinkInfo);
        log.info("01. bankAccountsLinkInfo = {}", bankAccountsLinkInfo);

        // 증권 계좌
        List<MydataInfoDto> securityAccountsLinkInfo = securityAccountService.linkMyDataAccount(userId,userAccountInfoDto.getSecurityAccounts());
        allMydataLinkInfo.addAll(securityAccountsLinkInfo);
        log.info("02. securityAccountsLinkInfo = {}", securityAccountsLinkInfo);

        // 카드
        List<MydataInfoDto> cardsLinkInfo = cardService.linkMyDataAccount(userId,userAccountInfoDto.getCards());
        allMydataLinkInfo.addAll(cardsLinkInfo);
        log.info("03. cardsLinkInfo = {}", cardsLinkInfo);

        // 연금
        List<MydataInfoDto> pensionsLinkInfo = pensionService.linkMyDataAccount(userId,userAccountInfoDto.getPensions());
        allMydataLinkInfo.addAll(pensionsLinkInfo);
        log.info("04. pensionsLinkInfo = {}", pensionsLinkInfo);

        // 대출
        List<MydataInfoDto> loansLinkInfo = loanService.linkMyDataAccount(userId,userAccountInfoDto.getLoans());
        allMydataLinkInfo.addAll(loansLinkInfo);
        log.info("05. loansLinkInfo = {}", loansLinkInfo);

        return allMydataLinkInfo;
    }



    @Override
    public Integer getTotalAssets(int userId) {
        Integer bankAccountTotalBalance = bankAccountService.getBankAccountsBalance(userId);
        log.info("bankAccountTotalBalance = {}",bankAccountTotalBalance);
        Integer securityAccountTotalBalance = securityAccountService.getSecurityAccountsBalance(userId);
        log.info("securityAccountTotalBalance = {}", securityAccountTotalBalance);
        return bankAccountTotalBalance + securityAccountTotalBalance;
    }

    @Override
    public List<PensionDto> getUnclaimedRetirementAccounts(int userId) {
        List<PensionDto> unclaimedRetirementAccounts = pensionService.getUnclaimedRetirementAccounts(userId);
        return unclaimedRetirementAccounts;
    }


}
