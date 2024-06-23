package com.pda.asset_service.service;



import com.pda.asset_service.dto.CardDto;
import com.pda.asset_service.dto.CardResponseDto;
import com.pda.asset_service.dto.MydataInfoDto;
import com.pda.asset_service.jpa.Card;

import java.util.List;

public interface CardService {

    Card convertToEntity(CardResponseDto cardResponseDto);

    CardDto convertToDto(Card card);

    List<MydataInfoDto> linkMyDataAccount(int userId, List<String> cards);

    List<CardDto> getCards(int userId);
}
