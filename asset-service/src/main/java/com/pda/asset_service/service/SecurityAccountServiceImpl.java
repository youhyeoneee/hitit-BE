package com.pda.asset_service.service;


import com.pda.asset_service.dto.*;
import com.pda.asset_service.feign.MydataServiceClient;
import com.pda.asset_service.jpa.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class SecurityAccountServiceImpl implements SecurityAccountService{

    private final SecurityAccountRepository securityAccountRepository;
    private final MydataInfoRepository mydataInfoRepository;
    private final SecurityTransactionRepository securityTransactionRepository;
    private final SecurityStockRepository securityStockRepository;
    private final MydataServiceClient mydataServiceClient;
    @Override
    public SecurityAccount convertToEntity(SecurityAccountResponseDto securityAccountResponseDto) {
        return SecurityAccount.builder()
                .accountNo(securityAccountResponseDto.getAccountNo())
                .securityName(securityAccountResponseDto.getSecurityName())
                .accountType(securityAccountResponseDto.getAccountType())
                .balance(securityAccountResponseDto.getBalance())
                .createdAt(securityAccountResponseDto.getCreatedAt())
                .userId(securityAccountResponseDto.getUserId())
                .build();
    }

    @Override
    public SecurityAccountDto convertToDto(SecurityAccount securityAccount) {
        return SecurityAccountDto.builder()
                .accountNo(securityAccount.getAccountNo())
                .securityName(securityAccount.getSecurityName())
                .accountType(securityAccount.getAccountType())
                .balance(securityAccount.getBalance())
                .createdAt(securityAccount.getCreatedAt())
                .userId(securityAccount.getUserId())
                .build();
    }

    @Override
    public List<MydataInfoDto> linkMyDataAccount(int userId, List<String> securityAccounts) {
        List<MydataInfoDto> securityAccountsLinkInfo = new ArrayList<>();

        // 증권 계좌 정보 처리

        for (String securityName : securityAccounts) {
            if( !securityName.isEmpty() ) {
                log.info("userAccount = {}", securityName);
                Optional<List<SecurityAccountResponseDto>> securityAccountsResponse = mydataServiceClient.getSecurityAccountsByUserIdAndSecurityName(userId, securityName);
//            log.info("securityAccounts Response From Mydata-service = {}", securityAccountsResponse);
                if (securityAccountsResponse.isPresent()) {
                    for (SecurityAccountResponseDto securityAccountResponseDto : securityAccountsResponse.get()) {
                        log.info("security account convert Entity = {}", securityAccountResponseDto);
                        SecurityAccount securityAccount = convertToEntity(securityAccountResponseDto);
                        securityAccountRepository.save(securityAccount);

                        mydataInfoRepository.save(MydataInfo.builder()
                                .assetType("security_accounts")
                                .userId(userId)
                                .companyName(securityAccount.getSecurityName())
                                .accountType(securityAccount.getAccountType())
                                .accountNo(securityAccount.getAccountNo())
                                .build());

                        MydataInfo savedInfo = mydataInfoRepository.findSecurityByUserIdAndAssetTypeAndCompanyNameAndAccountNo(
                                securityAccount.getUserId(),
                                "security_accounts",
                                securityAccount.getSecurityName(),
                                securityAccount.getAccountNo()
                        );
                        MydataInfoDto mydataInfoDto = MydataInfoDto.builder()
                                .assetType(savedInfo.getAssetType())
                                .userId(savedInfo.getUserId())
                                .companyName(savedInfo.getCompanyName())
                                .accountType(savedInfo.getAccountType())
                                .accountNo(securityAccount.getAccountNo())
                                .build();

                        securityAccountsLinkInfo.add(mydataInfoDto);

                        // 거래내역, 보유주식 데이터 가져오기
                        linkSecurityTransactions(securityAccount.getAccountNo());
                        linkSecurityStocks(securityAccount.getAccountNo());
                    }
                }
            }else{
                log.info("요청된 증권 계좌 없음");
            }
        }
        return securityAccountsLinkInfo;
    }


    public void linkSecurityTransactions(String accountNo){
        log.info("GGGGGGGGGGGGGGOOOOOOOOOOOOOOOOOOOOOOO = {}", accountNo);
        Optional<List<SecurityTransactionResponseDto>> linkedSecurityTransactions = mydataServiceClient.getSecurityTransactions(accountNo);
        if(linkedSecurityTransactions.isPresent()){
            for(SecurityTransactionResponseDto securityTransaction : linkedSecurityTransactions.get()){
                SecurityAccount securityAccount = securityAccountRepository.findByAccountNo(accountNo);
                securityTransactionRepository.save(SecurityTransaction.builder()
                        .id(securityTransaction.getId())

                        .txDatetime(securityTransaction.getTxDatetime())
                                .txType(securityTransaction.getTxType())
                                .txAmount(securityTransaction.getTxAmount())
                                .balAfterTx(securityTransaction.getBalAfterTx())
                                .securityAccount(securityAccount)
                                .stockCode(securityTransaction.getStockCode())
                        .build());
            }
        }else {
            log.info("해당 계좌 거래 내역 없음");
        }
//        return linkedSecurityTransactions.orElse(null);
    }


    public void linkSecurityStocks(String accountNo){
        Optional<List<SecurityStockResponseDto>> linkedSecurityStocks  = mydataServiceClient.getSecurityStocks(accountNo);
        if(linkedSecurityStocks.isPresent()){
            for(SecurityStockResponseDto securityStock : linkedSecurityStocks.get()){
                SecurityAccount securityAccount = securityAccountRepository.findByAccountNo(accountNo);

                SecurityStock newSecurityStock = SecurityStock.builder()
                                .id(SecurityStockId.builder()
                                .securityAccount(securityAccount)
                                .stockCode(securityStock.getStockCode())
                                .build())
                                .build();
                securityStockRepository.save(newSecurityStock);
            }
        }else {
            log.info("해당 계좌 보유 주식 없음");
        }
//        return linkedSecurityStocks.orElse(null);
    }

    @Override
    public List<SecurityAccountDto> getSecurityAccounts(int userId) {

        List<SecurityAccount> securityAccounts = securityAccountRepository.findByUserId(userId).orElse(null);

        List<SecurityAccountDto> securityAccountDtos = new ArrayList<>();
        if (securityAccounts != null) {
            for (SecurityAccount securityAccount : securityAccounts) {
                SecurityAccountDto securityAccountDto = convertToDto(securityAccount);
                log.info("find security account = {}", securityAccountDto);
                securityAccountDtos.add(securityAccountDto);
            }
        }
        return securityAccountDtos;
    }

    @Override
    public Integer getSecurityAccountsBalance(int userId) {
        Integer securityAccountsTotalBalance = 0;
        List<SecurityAccount> securityAccounts = securityAccountRepository.findByUserId(userId).orElse(null);

        if(securityAccounts != null){
            for (SecurityAccount securityAccount : securityAccounts){
                securityAccountsTotalBalance += securityAccount.getBalance();
            }
        }
        return securityAccountsTotalBalance;
    }

    @Override
    public SecurityAccountDto getSecurityAccountShinhanDC(int userId) {
        SecurityAccount securityAccount = securityAccountRepository.findByUserIdAndSecurityNameAndAccountType(userId, "신한투자증권", "DC").orElse(null);
        if(securityAccount != null){
            return SecurityAccountDto.builder()
                    .accountNo(securityAccount.getAccountNo())
                    .accountType(securityAccount.getAccountType())
                    .balance(securityAccount.getBalance())
                    .securityName(securityAccount.getSecurityName())
                    .userId(userId)
                    .createdAt(securityAccount.getCreatedAt())
                    .build();
        }else{
            return null;
        }
    }


}
