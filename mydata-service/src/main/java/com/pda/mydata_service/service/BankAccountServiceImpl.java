package com.pda.mydata_service.service;//package com.pda.mydata_service.legacy.service;

import com.pda.mydata_service.jpa.BankAccount;
import com.pda.mydata_service.jpa.BankAccountRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class BankAccountServiceImpl implements BankAccountService{

    private final BankAccountRepository bankAccountRepository;

    @Override
    public Optional<List<BankAccount>> getAllBankAccounts(int userId) {
        Optional<List<BankAccount>> allBankAccounts = bankAccountRepository.findByMydataUser_Id(userId);
        return allBankAccounts;
    }
}
