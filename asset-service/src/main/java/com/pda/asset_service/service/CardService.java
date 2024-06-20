package com.pda.asset_service.service;

import com.pda.asset_service.dto.*;
import com.pda.asset_service.jpa.BankAccount;
import com.pda.asset_service.jpa.Card;

import java.util.List;

public interface CardService {

    Card convertToEntity(CardResponseDto cardResponseDto);

    CardDto convertToDto(Card card);

    List<MydataInfoDto> linkMyDataAccount(int userId, List<String> cards);
}
