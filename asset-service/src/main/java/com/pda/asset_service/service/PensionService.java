package com.pda.asset_service.service;

import com.pda.asset_service.dto.*;
import com.pda.asset_service.jpa.BankAccount;
import com.pda.asset_service.jpa.Pension;

import java.util.List;

public interface PensionService {

    Pension convertToEntity(PensionResponseDto pensionResponseDto);

    PensionDto convertToDto(Pension pension);

    List<MydataInfoDto> linkMyDataAccount(int userId, List<String> pensions);

    List<PensionDto> getPensions(int userId);
}
