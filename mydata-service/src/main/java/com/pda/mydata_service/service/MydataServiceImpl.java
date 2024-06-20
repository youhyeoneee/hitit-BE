package com.pda.mydata_service.service;//package com.pda.mydata_service.legacy.service;


import com.pda.mydata_service.dto.BankAccountDto;
import com.pda.mydata_service.jpa.BankAccount;
import com.pda.mydata_service.jpa.BankAccountRepository;
import com.pda.mydata_service.jpa.MydataUser;
import com.pda.mydata_service.jpa.MydataUserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class MydataServiceImpl implements MydataService{

    private final BankAccountRepository bankAccountRepository;
    private final MydataUserRepository mydataUserRepository;

    @Override
    public BankAccount convertToEntity(BankAccountDto bankAccountDto) {
        MydataUser user = mydataUserRepository.findById(bankAccountDto.getUserId()).orElseThrow();
        return BankAccount.builder()
                .accountNo(bankAccountDto.getAccountNo())
                .bankName(bankAccountDto.getBankName())
                .accountType(bankAccountDto.getAccountType())
                .name(bankAccountDto.getName())
                .balance(bankAccountDto.getBalance())
                .createdAt(bankAccountDto.getCreatedAt())
                .mydataUser(user)
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
                .userId(bankAccount.getMydataUser().getId())
                .build();
    }

    @Override
    public Optional<List<BankAccountDto>> getAllBankAccounts(int userId) {
        Optional<List<BankAccount>> bankAccounts = bankAccountRepository.findByMydataUser_Id(userId);
        if (bankAccounts.isPresent()) {
            List<BankAccountDto> bankAccountDtos = bankAccounts.get().stream()
                    .map(this::convertToDto)
                    .toList();
            return Optional.of(bankAccountDtos);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Optional<List<BankAccountDto>> getBankAccountsByUserIdAndBankName(int userId, String bankName) {

        log.info("mydata service");


        Optional<List<BankAccount>> bankAccounts = bankAccountRepository.findByMydataUser_IdAndBankName(userId, bankName);
        log.info("bank accounts = {}", bankAccounts.get());
        if (bankAccounts.isPresent()) {
            List<BankAccountDto> bankAccountDtos = bankAccounts.get().stream()
                    .map(this::convertToDto)
                    .toList();
            log.info("bank accounts bankAccountDtos = {}", bankAccountDtos);
            return Optional.of(bankAccountDtos);
        } else {
            return Optional.empty();
        }
    }

}
