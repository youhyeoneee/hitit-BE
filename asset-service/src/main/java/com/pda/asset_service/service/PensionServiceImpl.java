package com.pda.asset_service.service;

import com.pda.asset_service.dto.MydataInfoDto;
import com.pda.asset_service.dto.PensionDto;
import com.pda.asset_service.dto.PensionResponseDto;
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
public class PensionServiceImpl implements PensionService{

    private final PensionRepository pensionRepository;
    private final AssetUserRepository assetUserRepository;
    private final MydataInfoRepository mydataInfoRepository;
    private final MydataServiceClient mydataServiceClient;

    @Override
    public Pension convertToEntity(PensionResponseDto pensionResponseDto) {
        AssetUser user = assetUserRepository.findById(pensionResponseDto.getUserId()).orElseThrow();
        return Pension.builder()
                .companyName(pensionResponseDto.getCompanyName())
                .pensionName(pensionResponseDto.getPensionName())
                .pensionType(pensionResponseDto.getPensionType())
                .assetUser(user)
                .interestRate(pensionResponseDto.getInterestRate())
                .evaluationAmount(pensionResponseDto.getEvaluationAmount())
                .expirationDate(pensionResponseDto.getExpirationDate())
                .accountNo(pensionResponseDto.getAccountNo())
                .build();
    }

    @Override
    public PensionDto convertToDto(Pension pension) {
        return PensionDto.builder()
                .companyName(pension.getCompanyName())
                .pensionName(pension.getPensionName())
                .pensionType(pension.getPensionType())
                .userId(pension.getAssetUser().getId())
                .interestRate(pension.getInterestRate())
                .evaluationAmount(pension.getEvaluationAmount())
                .expirationDate(pension.getExpirationDate())
                .accountNo(pension.getAccountNo())
                .build();
    }

    @Override
    public List<MydataInfoDto> linkMyDataAccount(int userId, List<String> pensions) {
        List<MydataInfoDto> pensionLinkInfo = new ArrayList<>();

        // 연금 정보 처리

        for (String pensionName : pensions) {
            if (!pensionName.isEmpty()) {
                log.info("userPension = {}", pensionName);
                Optional<List<PensionResponseDto>> pensionResponse = mydataServiceClient.getPensionsByUserIdAndCompanyName(userId, pensionName);
                if (pensionResponse.isPresent()) {
                    log.info("==========================================================================");
                    for (PensionResponseDto pensionResponseDto : pensionResponse.get()) {
                        log.info("pension convert Entity = {}", pensionResponseDto);
                        Pension pension = convertToEntity(pensionResponseDto);
                        pensionRepository.save(pension);
                        mydataInfoRepository.save(MydataInfo.builder()
                                .assetType("pensions")
                                .userId(pension.getAssetUser().getId())
                                .companyName(pension.getCompanyName())
                                .accountType(pension.getPensionType())
                                .accountNo(pension.getAccountNo())
                                .build());

                        MydataInfo savedInfo = mydataInfoRepository.findPensionByUserIdAndAssetTypeAndCompanyNameAndAccountNo(
                                pension.getAssetUser().getId(),
                                "pensions",
                                pension.getCompanyName(),
                                pension.getAccountNo()
                        );
                        MydataInfoDto mydataInfoDto = MydataInfoDto.builder()
                                .assetType(savedInfo.getAssetType())
                                .userId(savedInfo.getUserId())
                                .companyName(savedInfo.getCompanyName())
                                .accountType(savedInfo.getAccountType())
                                .accountNo(pension.getAccountNo())
                                .build();

                        pensionLinkInfo.add(mydataInfoDto);
                    }
                }
            }
        }
        return pensionLinkInfo;
    }

}
