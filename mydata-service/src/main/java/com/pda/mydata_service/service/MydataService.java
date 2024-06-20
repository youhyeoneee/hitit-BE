package com.pda.mydata_service.service;//package com.pda.mydata_service.legacy.service;

import com.pda.mydata_service.dto.*;
import com.pda.mydata_service.jpa.BankAccount;

import java.util.List;
import java.util.Optional;

public interface MydataService {

    BankAccount convertToEntity(BankAccountDto bankAccountDto);

    BankAccountDto convertToDto(BankAccount bankAccount);

    Optional<List<BankAccountDto>> getAllBankAccounts(int userId);

    Optional<List<BankAccountDto>> getBankAccountsByUserIdAndBankName(int userId, String bankName);

    Optional<List<LoanDto>> getLoansByUserIdAndCompanyName(int userId, String companyName);

    Optional<List<CardDto>> getCardsByUserIdAndCompanyName(int userId, String companyName);

    Optional<List<PensionDto>> getPensionsByUserIdAndCompanyName(int userId, String companyName);

    Optional<List<SecurityAccountDto>> getSecurityAccountsByUserIdAndSecurityName(int userId, String securityName);
}
