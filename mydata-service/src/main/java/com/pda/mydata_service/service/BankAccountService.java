package com.pda.mydata_service.service;//package com.pda.mydata_service.legacy.service;

import com.pda.mydata_service.jpa.BankAccount;

import java.util.List;
import java.util.Optional;

public interface BankAccountService {
    Optional<List<BankAccount>> getAllBankAccounts(int userId);

}
