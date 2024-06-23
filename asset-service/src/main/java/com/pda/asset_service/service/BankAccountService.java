package com.pda.asset_service.service;



import com.pda.asset_service.dto.*;
import com.pda.asset_service.jpa.*;

import java.util.List;

public interface BankAccountService {

    BankAccount convertToEntity(BankAccountResponseDto bankAccountDto);

    BankAccountDto convertToDto(BankAccount bankAccount);

    List<MydataInfoDto> linkMyDataAccount(int userId, List<String> bankAccounts);

    List<BankAccountDto> getBankAccounts(int userId);

    Integer getBankAccountsBalance(int userId);
}
