package com.pda.asset_service.service;

import com.pda.asset_service.dto.BankAccountDto;
import com.pda.asset_service.dto.BankAccountResponseDto;
import com.pda.asset_service.dto.MydataInfoDto;
import com.pda.asset_service.dto.SecurityAccountDto;
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
public class BankAccountServiceImpl implements BankAccountService{


    private final BankAccountRepository bankAccountRepository;
    private final AssetUserRepository assetUserRepository;
    private final MydataInfoRepository mydataInfoRepository;
    private final MydataServiceClient mydataServiceClient;

    @Override
    public BankAccount convertToEntity(BankAccountResponseDto bankAccountDto) {
        log.info("BankAccountServiceImpl User Id = {}", bankAccountDto.getUserId());
        AssetUser user = assetUserRepository.findById(bankAccountDto.getUserId()).orElseThrow();
        return BankAccount.builder()
                .accountNo(bankAccountDto.getAccountNo())
                .bankName(bankAccountDto.getBankName())
                .accountType(bankAccountDto.getAccountType())
                .name(bankAccountDto.getName())
                .balance(bankAccountDto.getBalance())
                .createdAt(bankAccountDto.getCreatedAt())
                .assetUser(user)
                .build();
    }

    @Override
    public BankAccountDto convertToDto(BankAccount bankAccount) {
        return BankAccountDto.builder()
                .accountNo(bankAccount.getAccountNo())
                .bankName(bankAccount.getBankName())
                .accountType(bankAccount.getAccountType())
                .name(bankAccount.getName())
                .balance(bankAccount.getBalance())
                .createdAt(bankAccount.getCreatedAt())
                .userId(bankAccount.getAssetUser().getId())
                .build();
    }

    @Override
    public List<MydataInfoDto> linkMyDataAccount(int userId, List<String> bankAccounts) {

        List<MydataInfoDto> bankAccountsLinkInfo = new ArrayList<>();

        // 은행 계좌 정보 처리

            for (String bankName : bankAccounts) {
                if (!bankName.isEmpty()) {
                    log.info("userAccount = {}", bankName);
                    // 하나의 은행에 여러 계좌가 있으면 리스트로 여러 개가 온다..
                    Optional<List<BankAccountResponseDto>> bankAccountsResponse = mydataServiceClient.getBankAccountsByUserIdAndBankName(userId, bankName);
                    log.info("bankAccounts Response From Mydata-service = {}", bankAccountsResponse);
                    if (bankAccountsResponse.isPresent()) {
                        log.info("==========================================================================");
                        for (BankAccountResponseDto bankAccountDto : bankAccountsResponse.get()) {
                            log.info("bank account convert Entity = {}", bankAccountDto);
                            BankAccount bankAccount = convertToEntity(bankAccountDto);
                            log.info("========================== = {}", bankAccount);
                            bankAccountRepository.save(bankAccount);
                            log.info("bank account Saved = {}", bankAccount);

                            mydataInfoRepository.save(MydataInfo.builder()
                                    .assetType("bank_accounts")
                                    .userId(bankAccount.getAssetUser().getId())
                                    .companyName(bankAccount.getBankName())
                                    .accountType(bankAccount.getAccountType())
                                    .accountNo(bankAccount.getAccountNo())
                                    .build());

                            MydataInfo savedInfo = mydataInfoRepository.findBankAccountByUserIdAndAssetTypeAndCompanyNameAndAccountNo(
                                    bankAccount.getAssetUser().getId(),
                                    "bank_accounts",
                                    bankAccount.getBankName(),
                                    bankAccount.getAccountNo()
                            );
                            MydataInfoDto mydataInfoDto = MydataInfoDto.builder()
                                    .assetType(savedInfo.getAssetType())
                                    .userId(savedInfo.getUserId())
                                    .companyName(savedInfo.getCompanyName())
                                    .accountType(savedInfo.getAccountType())
                                    .accountNo(bankAccount.getAccountNo())
                                    .build();

                            bankAccountsLinkInfo.add(mydataInfoDto);
                        }
                    }
                } else {
                    log.info("요청된 은행 계좌 없음");
                }

            }

        return bankAccountsLinkInfo;

    }

    @Override
    public List<BankAccountDto> getBankAccounts(int userId) {
        List<BankAccount> bankAccounts = bankAccountRepository.findByAssetUserId(userId).orElse(null);

        List<BankAccountDto> bankAccountDtos = new ArrayList<>();
        if (bankAccounts != null) {
            for (BankAccount bankAccount : bankAccounts) {
                BankAccountDto bankAccountDto = convertToDto(bankAccount);
                log.info("find security account = {}", bankAccountDto);
                bankAccountDtos.add(bankAccountDto);
            }
        }
        return bankAccountDtos;
    }
}
