package com.pda.asset_service.service;

import com.pda.asset_service.dto.AccountCreateDto;
import com.pda.asset_service.dto.AccountDto;
import com.pda.asset_service.dto.PensionDto;
import com.pda.asset_service.dto.SecurityAccountDto;
import com.pda.asset_service.jpa.Account;
import com.pda.asset_service.jpa.AccountRepository;
import com.pda.asset_service.jpa.SecurityAccount;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@AllArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService{

    private final AccountRepository accountRepository;
    private final SecurityAccountServiceImpl securityAccountService;


    @Override
    public SecurityAccountDto getRetirementAccountShinhan(int userId) {
        SecurityAccountDto securityAccount = securityAccountService.getSecurityAccountShinhanDC(userId);

        return securityAccount;
    }

    @Override
    @Transactional
    public AccountDto createAccount(int userId, AccountCreateDto accountCreateDto) {

        Account checkAccount = accountRepository.findByUserId(userId).orElse(null);

        if (checkAccount != null) {
            throw new IllegalArgumentException("이미 계좌가 존재합니다.");
        }

        LocalDateTime now = LocalDateTime.now();
        Account createAccount =
                Account.builder()
                        .accountNo(generateAccountNumber())
                        .ssn(accountCreateDto.getSsn())
                        .name(accountCreateDto.getName())
                        .password(accountCreateDto.getPassword())
                        .balance(accountCreateDto.getBalance())
                        .companyName("신한투자증권")
                        .pensionType("DC")
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .userId(userId)
                        .build();
        log.info("createAccount = {}", createAccount);
        accountRepository.save(createAccount);

        // 저장된 계좌 정보를 다시 조회하여 AccountDto로 반환
        Account foundAccount = accountRepository.findByAccountNo(createAccount.getAccountNo())
                .orElseThrow(() -> new IllegalStateException("계좌 정보를 찾을 수 없습니다."));
        return AccountDto.builder()
                .accountNo(foundAccount.getAccountNo())
                .ssn(foundAccount.getSsn())
                .name(foundAccount.getName())
                .balance(foundAccount.getBalance())
                .companyName(foundAccount.getCompanyName())
                .pensionType(foundAccount.getPensionType())
                .build();
    }

    @Override
    public AccountDto getAccount(int userId) {
        Account foundAccount = accountRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalStateException("계좌 정보를 찾을 수 없습니다."));
        return AccountDto.builder()
                .accountNo(foundAccount.getAccountNo())
                .ssn(foundAccount.getSsn())
                .name(foundAccount.getName())
                .balance(foundAccount.getBalance())
                .companyName(foundAccount.getCompanyName())
                .pensionType(foundAccount.getPensionType())
                .build();
    }

    public String generateAccountNumber() {
        Random random = new Random();

        int firstThree = random.nextInt(900) + 100;
        int middleTwo = random.nextInt(90) + 10;
        int lastSix = random.nextInt(900000) + 100000;

        return String.format("%03d-%02d-%06d", firstThree, middleTwo, lastSix);
    }
}
