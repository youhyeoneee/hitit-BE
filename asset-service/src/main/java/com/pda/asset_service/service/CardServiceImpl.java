package com.pda.asset_service.service;

import com.pda.asset_service.dto.CardDto;
import com.pda.asset_service.dto.CardResponseDto;
import com.pda.asset_service.dto.MydataInfoDto;
import com.pda.asset_service.dto.SecurityAccountDto;
import com.pda.asset_service.feign.MydataServiceClient;
import com.pda.asset_service.jpa.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class CardServiceImpl implements CardService{

    private final CardRepository cardRepository;
    private final AssetUserRepository assetUserRepository;
    private final MydataInfoRepository mydataInfoRepository;
    private final MydataServiceClient mydataServiceClient;
    @Override
    public Card convertToEntity(CardResponseDto cardResponseDto) {
        AssetUser assetUser = assetUserRepository.findById(cardResponseDto.getUserId()).orElseThrow();
        return Card.builder()
                .cardNo(cardResponseDto.getCardNo())
                .companyName(cardResponseDto.getCompanyName())
                .cardName(cardResponseDto.getCardName())
                .cardType(cardResponseDto.getCardType())
                .createdAt(cardResponseDto.getCreatedAt())
                .expiredAt(cardResponseDto.getExpiredAt())
                .accountNo(cardResponseDto.getAccountNo())
                .assetUser(assetUser)
                .build();
    }

    @Override
    public CardDto convertToDto(Card card) {
        return CardDto.builder()
                .cardNo(card.getCardNo())
                .companyName(card.getCompanyName())
                .cardName(card.getCardName())
                .cardType(card.getCardType())
                .createdAt(card.getCreatedAt())
                .expiredAt(card.getExpiredAt())
                .accountNo(card.getAccountNo())
                .userId(card.getAssetUser().getId())
                .build();
    }

    @Override
    public List<MydataInfoDto> linkMyDataAccount(int userId, List<String> cards) {
        List<MydataInfoDto> cardLinkInfo = new ArrayList<>();

        // 카드 정보 처리

        for (String cardName : cards) {
            if( !cardName.isEmpty() ) {
                log.info("userCard = {}", cardName);
                Optional<List<CardResponseDto>> cardResponse = mydataServiceClient.getCardsByUserIdAndCompanyName(userId, cardName);
                log.info("cards Response From Mydata-service = {}", cardResponse);
                if (cardResponse.isPresent()) {
                    log.info("==========================================================================");
                    for (CardResponseDto cardResponseDto : cardResponse.get()) {
                        log.info("card convert Entity = {}", cardResponseDto);
                        Card card = convertToEntity(cardResponseDto);
                        log.info("========================== = {}", card);
                        cardRepository.save(card);
                        log.info("card Saved = {}", card);

                        mydataInfoRepository.save(MydataInfo.builder()
                                .assetType("cards")
                                .userId(card.getAssetUser().getId())
                                .companyName(card.getCompanyName())
                                .accountType(card.getCardType())
                                .accountNo(card.getCardNo())
                                .build());

                        MydataInfo savedInfo = mydataInfoRepository.findCardByUserIdAndAssetTypeAndCompanyNameAndAccountNo(
                                card.getAssetUser().getId(),
                                "cards",
                                card.getCompanyName(),
                                card.getCardNo()
                        );
                        MydataInfoDto mydataInfoDto = MydataInfoDto.builder()
                                .assetType(savedInfo.getAssetType())
                                .userId(savedInfo.getUserId())
                                .companyName(savedInfo.getCompanyName())
                                .accountType(savedInfo.getAccountType())
                                .accountNo(card.getCardNo())
                                .build();

                        cardLinkInfo.add(mydataInfoDto);
                    }
                }
            }else {
                log.info("요청된 카드 계좌 없음");
            }
        }
        return cardLinkInfo;
    }

    @Override
    public List<CardDto> getCards(int userId) {
        List<Card> cards = cardRepository.findByAssetUserId(userId).orElse(null);

        List<CardDto> cardDtos = new ArrayList<>();
        if (cards != null) {
            for (Card card : cards) {
                CardDto cardDto = convertToDto(card);
                log.info("find card account = {}", cardDto);
                cardDtos.add(cardDto);
            }
        }
        return cardDtos;
    }

}
