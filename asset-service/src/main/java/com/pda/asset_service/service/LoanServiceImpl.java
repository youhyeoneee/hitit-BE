package com.pda.asset_service.service;

import com.pda.asset_service.dto.LoanDto;
import com.pda.asset_service.dto.LoanResponseDto;
import com.pda.asset_service.dto.MydataInfoDto;
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
public class LoanServiceImpl implements LoanService{

    private final LoanRepository loanRepository;
    private final AssetUserRepository assetUserRepository;
    private final MydataInfoRepository mydataInfoRepository;
    private final MydataServiceClient mydataServiceClient;

    @Override
    public Loan convertToEntity(LoanResponseDto loanResponseDto) {
        AssetUser user = assetUserRepository.findById(loanResponseDto.getUserId()).orElseThrow();
        return Loan.builder()
                .companyName(loanResponseDto.getCompanyName())
                .loanType(loanResponseDto.getLoanType())
                .loanAmount(loanResponseDto.getLoanAmount())
                .interestRate(loanResponseDto.getInterestRate())
                .totalPayments(loanResponseDto.getTotalPayments())
                .accountNo(loanResponseDto.getAccountNo())
                .assetUser(user)
                .build();
    }

    @Override
    public LoanDto convertToDto(Loan loan) {
        return LoanDto.builder()
                .companyName(loan.getCompanyName())
                .loanType(loan.getLoanType())
                .loanAmount(loan.getLoanAmount())
                .interestRate(loan.getInterestRate())
                .totalPayments(loan.getTotalPayments())
                .accountNo(loan.getAccountNo())
                .userId(loan.getAssetUser().getId())
                .build();
    }

    @Override
    public List<MydataInfoDto> linkMyDataAccount(int userId, List<String> loans) {
        List<MydataInfoDto> loanLinkInfo = new ArrayList<>();

        // 대출 정보 처리

        for (String loanName : loans) {
            if( !loans.isEmpty()) {
                log.info("userAccount = {}", loanName);
                Optional<List<LoanResponseDto>> loanResponse = mydataServiceClient.getLoansByUserIdAndCompanyName(userId, loanName);
                if (loanResponse.isPresent()) {
                    for (LoanResponseDto loanResponseDto : loanResponse.get()) {
                        Loan loan = convertToEntity(loanResponseDto);
                        loanRepository.save(loan);

                        mydataInfoRepository.save(MydataInfo.builder()
                                .assetType("loans")
                                .userId(loan.getAssetUser().getId())
                                .companyName(loan.getCompanyName())
                                .accountType(loan.getLoanType())
                                .accountNo(loan.getAccountNo())
                                .build());

                        MydataInfo savedInfo = mydataInfoRepository.findLoanByUserIdAndAssetTypeAndCompanyNameAndAccountNo(
                                loan.getAssetUser().getId(),
                                "loans",
                                loan.getCompanyName(),
                                loan.getAccountNo()
                        );
                        MydataInfoDto mydataInfoDto = MydataInfoDto.builder()
                                .assetType(savedInfo.getAssetType())
                                .userId(savedInfo.getUserId())
                                .companyName(savedInfo.getCompanyName())
                                .accountType(savedInfo.getAccountType())
                                .accountNo(loan.getAccountNo())
                                .build();

                        loanLinkInfo.add(mydataInfoDto);
                    }
                }
            }
        }
        return loanLinkInfo;
    }


}
