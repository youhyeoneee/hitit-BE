package com.pda.portfolio_service.service;

import com.pda.portfolio_service.dto.*;
import com.pda.portfolio_service.jpa.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Comparator;

@Slf4j
@Service
public class PortfolioService {
    @Autowired
    private PortfolioRepository portfolioRepository;

    @Autowired
    private PortfolioFundRepository portfolioFundRepository;

    @Autowired
    private PortfolioFundAssetRepository portfolioFundAssetRepository;

    @Autowired
    private PortfolioFundStockRepository portfolioFundStockRepository;

    @Autowired
    private PortfolioFundBondRepository portfolioFundBondRepository;

    @Autowired
    private UserPortfolioRepository userPortfolioRepository;

    public List<HititPortfoliosResponseDto> getHititPortfolios() {
        List<Portfolio> portfolios = portfolioRepository.findAll();

        return portfolios.stream()
                .map(portfolio -> {
                    // 각 포트폴리오의 id 값으로 해당 포트폴리오의 펀드 데이터를 가져옴
                    List<PortfolioFund> fundProducts = portfolioFundRepository.findByIdPortfolioId(portfolio.getId());

                    double totalReturn = fundProducts.stream()
                            .mapToDouble(fund -> fund.getWeight() * fund.getReturn3m())
                            .sum() / 100;

                    float totalReturnFloat = BigDecimal.valueOf(totalReturn)
                            .setScale(2, RoundingMode.HALF_UP)
                            .floatValue();

                    return new HititPortfoliosResponseDto(
                            portfolio.getId(),
                            portfolio.getName(),
                            portfolio.getInvestmentType(),
                            portfolio.getSummary(),
                            portfolio.getMinimumSubscriptionFee(),
                            portfolio.getStockExposure(),
                            totalReturnFloat
                    );
                })
                .collect(Collectors.toList());
    }

    public List<HititPortfoliosFundsResponseDto> getHititPortfoliosFunds(Integer portfolioId) {
        List<PortfolioFund> fundProducts = portfolioFundRepository.findByIdPortfolioId(portfolioId);

        return fundProducts.stream()
                .map(portfolioFund -> new HititPortfoliosFundsResponseDto(
                        portfolioFund.getId().getFundCode(),
                        portfolioFund.getId().getPortfolioId(),
                        portfolioFund.getFundName(),
                        portfolioFund.getFundTypeDetail(),
                        portfolioFund.getCompanyName(),
                        portfolioFund.getWeight(),
                        portfolioFund.getReturn3m()))
                .sorted(Comparator.comparingDouble(HititPortfoliosFundsResponseDto::getWeight).reversed()
                        .thenComparing(HititPortfoliosFundsResponseDto::getFundName, Comparator.nullsLast(Comparator.naturalOrder())))
                .collect(Collectors.toList());
    }

    public HititPortfoliosFundsStocksAndBondsResponseDto getHititPortfoliosFundsStocksAndBonds(Integer portfolioId, Integer fundId) {
        // private_portfolios_fund_products 테이블에서 portfolio_id에 해당하는 데이터 가져오기
        List<PortfolioFund> fundProducts = portfolioFundRepository.findByIdPortfolioId(portfolioId);

        // 가져온 private_portfolios_fund_products 데이터를 정렬
        fundProducts.sort(Comparator
                .comparingDouble(PortfolioFund::getWeight).reversed()
                .thenComparing(Comparator.comparing(PortfolioFund::getFundName, Comparator.nullsLast(Comparator.naturalOrder()))));

        // fundId 에러 처리
        if (fundId < 0 || fundId >= fundProducts.size()) {
            throw new IllegalArgumentException("유효하지 않은 fundId: " + fundId);
        }

        // FE에서 유저가 선택한 fund 데이터 인덱스로 가져오기
        PortfolioFund selectedFund = fundProducts.get(fundId);

        // 선택한 fund의 fund_code로 private_portfolios_fund_assets에서 fund_code에 해당하는 데이터 가져오기
        PortfolioFundAsset fundProductsAsset = portfolioFundAssetRepository.findByFundCode(selectedFund.getId().getFundCode());

        // 선택한 fund의 fund_code로 private_portfolios_fund_stocks에서 fund_code에 해당하는 데이터 가져오고 List<HititPortfoliosFundsStocksAndBondsResponseDto.FundStockDto> 형식으로 리스트로 저장
        Optional<List<PortfolioFundStock>> fundProductsStocks = portfolioFundStockRepository.findByIdFundCode(selectedFund.getId().getFundCode());
        List<HititPortfoliosFundsStocksAndBondsResponseDto.FundStockDto> fundStockDtos = fundProductsStocks.orElse(List.of()).stream()
                .map(stock -> new HititPortfoliosFundsStocksAndBondsResponseDto.FundStockDto(stock.getId().getStockName(), stock.getSize(), stock.getStyle(), stock.getWeight()))
                .collect(Collectors.toList());

        // 선택한 fund의 fund_code로 private_portfolios_fund_bonds에서 fund_code에 해당하는 데이터 가져오고 List<HititPortfoliosFundsStocksAndBondsResponseDto.FundBondDto> 형식으로 리스트로 저장
        Optional<List<PortfolioFundBond>> fundProductsBonds = portfolioFundBondRepository.findByIdFundCode(selectedFund.getId().getFundCode());
        List<HititPortfoliosFundsStocksAndBondsResponseDto.FundBondDto> fundBondDtos = fundProductsBonds.orElse(List.of()).stream()
                .map(bond -> new HititPortfoliosFundsStocksAndBondsResponseDto.FundBondDto(bond.getId().getBondName(), bond.getExpiredDate(), bond.getDuration(), bond.getCredit(), bond.getWeight()))
                .collect(Collectors.toList());

        // 모든 데이터를 저장할 HititPortfoliosFundsStocksAndBondsResponseDto 생성 후 담아서 Controller로 전달
        return new HititPortfoliosFundsStocksAndBondsResponseDto(
                selectedFund.getId().getFundCode(),
                selectedFund.getId().getPortfolioId(),
                selectedFund.getFundName(),
                selectedFund.getFundTypeDetail(),
                selectedFund.getCompanyName(),
                selectedFund.getWeight(),
                selectedFund.getReturn3m(),
                fundProductsAsset.getStock(),
                fundProductsAsset.getStockForeign(),
                fundProductsAsset.getBond(),
                fundProductsAsset.getBondForeign(),
                fundProductsAsset.getInvestment(),
                fundProductsAsset.getEtc(),
                fundStockDtos,
                fundBondDtos
        );
    }

    public PortfolioFundAssetResponseDto getUserPortfolioFundAssets(UserPortfolioFundRequestDto userPortfolioFundRequestDto) {
        UserPortfolio user = userPortfolioFundRequestDto.convertToEntity();

        // User의 Portfolio Id 가져오기
        UserPortfolio portfolioUser
                = userPortfolioRepository.findById(user.getId())
                .orElseThrow(()
                        -> new NoSuchElementException("유저가 존재하지 않습니다."));

        // Portfolio Id를 통해 Portfolio 찾기
        Portfolio portfolios = portfolioRepository.findById(portfolioUser.getPortfolio_id())
                .orElseThrow(()
                        -> new NoSuchElementException("유저에 해당하는 포트폴리오가 존재하지 않습니다."));

        // portfolio_id로 fund 리스트 가져오기
        List<PortfolioFund> fundProducts = portfolioFundRepository.findByIdPortfolioId(portfolios.getId());
//        int fundCount = fundProducts.size(); // 펀드 리스트의 길이

        // 펀드 리스트에서 fund_code로 assets 가져오기
        float totalStock = 0;
        float totalStockForeign = 0;
        float totalBond = 0;
        float totalBondForeign = 0;
        float totalInvestment = 0;
        float totalEtc = 0;

        for (PortfolioFund fund : fundProducts) {
            String fundCode = fund.getId().getFundCode();
            PortfolioFundAsset fundAsset = portfolioFundAssetRepository.findByFundCode(fundCode);

            float weight = fund.getWeight() / 100.0f;

            totalStock += fundAsset.getStock() * weight;
            totalStockForeign += fundAsset.getStockForeign() * weight;
            totalBond += fundAsset.getBond() * weight;
            totalBondForeign += fundAsset.getBondForeign() * weight;
            totalInvestment += fundAsset.getInvestment() * weight;
            totalEtc += fundAsset.getEtc() * weight;
        }

        // 결과를 DTO로 반환
        // DecimalFormat을 사용하여 소숫점 2자리까지만 표시
        DecimalFormat df = new DecimalFormat("#.##");

        // 결과를 DTO로 반환
        PortfolioFundAssetResponseDto responseDto = new PortfolioFundAssetResponseDto(
                Float.parseFloat(df.format(totalStock)),
                Float.parseFloat(df.format(totalStockForeign)),
                Float.parseFloat(df.format(totalBond)),
                Float.parseFloat(df.format(totalBondForeign)),
                Float.parseFloat(df.format(totalInvestment)),
                Float.parseFloat(df.format(totalEtc))
        );

        return responseDto;
    }
}