package com.pda.asset_service.service;

import com.pda.asset_service.dto.BankAccountDto;
import com.pda.asset_service.dto.MydataInfoDto;
import com.pda.asset_service.dto.UserAccountInfoDto;
import com.pda.asset_service.feign.MydataServiceClient;
import com.pda.asset_service.jpa.BankAccountRepository;
import com.pda.asset_service.jpa.MydataInfo;
import com.pda.asset_service.jpa.MydataInfoRepository;
import com.pda.user_service.jpa.User;
import com.pda.user_service.jpa.UserRepository;
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

    private final UserRepository userRepository;

    @Override
    @Transactional
    public List<MydataInfoDto> linkMydata(UserAccountInfoDto userAccountInfoDto) {

        int userId = userAccountInfoDto.getUserId();
        log.info("userId = {}", userId);

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

//        // 펀드
//        List<MydataInfoDto> fundsLinkInfo = fundService.linkMyDataAccount(userId,userAccountInfoDto.getFunds());
//        allMydataLinkInfo.addAll(fundsLinkInfo);
//        log.info("06. fundsLinkInfo = {}", fundsLinkInfo);

        // mq로 요청 필요...

       User updatedUser =  updateMydataStatus(userId);
       log.info("Mydata Link Updated User Info = {}", updatedUser);
        return allMydataLinkInfo;
    }

    @Override
    public User updateMydataStatus(int userId) {
        // mdyata info 테이블 결과 있는지 확인
        List<MydataInfo> mydataLinkedList = mydataInfoRepository.findByUserId(userId);

        // user repository 값 변경하라고 요청
        if (!mydataLinkedList.isEmpty()) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            user.setMydata("Y");
            return userRepository.save(user);
        }

        return null;
    }



}
