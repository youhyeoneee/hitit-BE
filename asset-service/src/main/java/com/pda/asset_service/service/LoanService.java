package com.pda.asset_service.service;


import com.pda.asset_service.dto.*;
import com.pda.asset_service.jpa.BankAccount;
import com.pda.asset_service.jpa.Loan;

import java.util.List;

public interface LoanService {

    Loan convertToEntity(LoanResponseDto loanResponseDto);

    LoanDto convertToDto(Loan loan);

    List<MydataInfoDto> linkMyDataAccount(int userId, List<String> loans);

    List<LoanDto> getLoans(int userId);
}
