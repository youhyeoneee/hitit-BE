package com.pda.portfolio_service.service;

import com.pda.portfolio_service.dto.*;
import com.pda.portfolio_service.dto_test.MyDataTestDto;
import com.pda.portfolio_service.feign.*;
import com.pda.portfolio_service.jpa.*;
import com.pda.utils.rabbitmq.dto.NotificationDto;
import com.pda.utils.rabbitmq.service.MessageService;
import lombok.RequiredArgsConstructor;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
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
    private FundStocksRepository fundStocksRepository;

    @Autowired
    private FundBondsRepository fundBondsRepository;

    @Autowired
    private UserPortfoliosRepository userPortfoliosRepository;

    @Autowired
    private UserPortfoliosFundProductsRepository userPortfoliosFundProductsRepository;

    @Autowired
    private FundProductsRepository fundProductsRepository;

    @Autowired
    private FlaskTestServiceClient flaskTestServiceClient;

    @Autowired
    private MyDataPortfolioServiceClient myDataPortfolioServiceClient;

    @Autowired
    private OptimizeServiceClient optimizeServiceClient;

    private final MessageService messageService;


    //// 1. 자체 서비스 - 포트폴리오 조회
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



    //// 2. 자체 서비스 - 포트폴리오 내 펀드 조회
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

    //// 3. 자체 서비스 - 포트폴리오 내 펀드 내 주식, 채권 조회
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


    //// 4. 자산 - 내 포트폴리오 조회
    public PortfolioFundAssetResponseDto getUserPortfolioFundAssets(int userId) {

        // 1. User의 Portfolio Id 가져오기
        UserPortfolios userPortfolios
                = userPortfoliosRepository.findByUserId(userId)
                .orElseThrow(()
                        -> new NoSuchElementException("포트폴리오가 존재하지 않습니다."));


        // 2. Portfolio Id를 통해 펀드리스트 가져오기
        List<UserPortfoliosFundProducts> fundProducts = userPortfoliosFundProductsRepository.findByIdPortfolioId(userPortfolios.getId());


        // 펀드 리스트에서 fund_code로 assets 가져오기
        float totalStock = 0;
        float totalStockForeign = 0;
        float totalBond = 0;
        float totalBondForeign = 0;
        float totalInvestment = 0;
        float totalEtc = 0;

        for (UserPortfoliosFundProducts fund : fundProducts) {
            String fundCode = fund.getId().getFundCode();

            FundProducts userPortfoliosFundAssets
                    = fundProductsRepository.findById(fundCode)
                    .orElseThrow(()
                    -> new NoSuchElementException("펀드가 존재하지 않습니다."));;

            float weight = fund.getWeight() / 100.0f;

            totalStock += userPortfoliosFundAssets.getStock() * weight;
            totalStockForeign += userPortfoliosFundAssets.getStockForeign() * weight;
            totalBond += userPortfoliosFundAssets.getBond() * weight;
            totalBondForeign += userPortfoliosFundAssets.getBondForeign() * weight;
            totalInvestment += userPortfoliosFundAssets.getInvestment() * weight;
            totalEtc += userPortfoliosFundAssets.getEtc() * weight;
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


    //// 5. 자산 - 내 포트폴리오 내 펀드 조회
    public List<HititPortfoliosFundsResponseDto> getUserPortfolioFundProducts(int userId) {
        // 1. User의 Portfolio Id 가져오기
        UserPortfolios userPortfolios
                = userPortfoliosRepository.findByUserId(userId)
                .orElseThrow(()
                        -> new NoSuchElementException("유저가 존재하지 않습니다."));


        // 2. Portfolio Id를 통해 펀드리스트 가져오기
        List<UserPortfoliosFundProducts> fundProducts = userPortfoliosFundProductsRepository.findByIdPortfolioId(userPortfolios.getId());


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


    //// 6. 자산 - 내 포트폴리오 내 펀드 내 주식, 채권 조회
    public HititPortfoliosFundsStocksAndBondsResponseDto getUserPortfolioFundStocksAndBonds(Integer userId, Integer fundId) {
        // 1. User의 Portfolio Id 가져오기
        UserPortfolios userPortfolios
                = userPortfoliosRepository.findByUserId(userId)
                .orElseThrow(()
                        -> new NoSuchElementException("유저가 존재하지 않습니다."));


        // 2. Portfolio Id를 통해 펀드리스트 가져오기
        List<UserPortfoliosFundProducts> fundProducts = userPortfoliosFundProductsRepository.findByIdPortfolioId(userPortfolios.getId());


        // 가져온 private_portfolios_fund_products 데이터를 정렬
        fundProducts.sort(Comparator
                .comparingDouble(UserPortfoliosFundProducts::getWeight).reversed()
                .thenComparing(Comparator.comparing(UserPortfoliosFundProducts::getFundName, Comparator.nullsLast(Comparator.naturalOrder()))));


        // FE에서 유저가 선택한 fund 데이터 인덱스로 가져오기
        UserPortfoliosFundProducts selectedFund = fundProducts.get(fundId);

        FundProducts userPortfoliosFundAssets
                = fundProductsRepository.findById(selectedFund.getId().getFundCode())
                .orElseThrow(()
                        -> new NoSuchElementException("펀드가 존재하지 않습니다."));;

        // 선택한 fund의 fund_code로 private_portfolios_fund_stocks에서 fund_code에 해당하는 데이터 가져오고 List<HititPortfoliosFundsStocksAndBondsResponseDto.FundStockDto> 형식으로 리스트로 저장
        Optional<List<FundStocks>> fundProductsStocks = fundStocksRepository.findByIdFundCode(selectedFund.getId().getFundCode());
        List<HititPortfoliosFundsStocksAndBondsResponseDto.FundStockDto> fundStockDtos = fundProductsStocks.orElse(List.of()).stream()
                .map(stock -> new HititPortfoliosFundsStocksAndBondsResponseDto.FundStockDto(stock.getId().getStockName(), stock.getSize(), stock.getStyle(), stock.getWeight()))
                .collect(Collectors.toList());

        // 선택한 fund의 fund_code로 private_portfolios_fund_bonds에서 fund_code에 해당하는 데이터 가져오고 List<HititPortfoliosFundsStocksAndBondsResponseDto.FundBondDto> 형식으로 리스트로 저장
        Optional<List<FundBonds>> fundProductsBonds = fundBondsRepository.findByIdFundCode(selectedFund.getId().getFundCode());
        List<HititPortfoliosFundsStocksAndBondsResponseDto.FundBondDto> fundBondDtos = fundProductsBonds.orElse(List.of()).stream()
                .map(bond -> new HititPortfoliosFundsStocksAndBondsResponseDto.FundBondDto(bond.getId().getBondName(), bond.getExpireDate(), bond.getDuration(), bond.getCredit(), bond.getWeight()))
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
                userPortfoliosFundAssets.getStock(),
                userPortfoliosFundAssets.getStockForeign(),
                userPortfoliosFundAssets.getBond(),
                userPortfoliosFundAssets.getBondForeign(),
                userPortfoliosFundAssets.getInvestment(),
                userPortfoliosFundAssets.getEtc(),
                fundStockDtos,
                fundBondDtos
        );
    }

    //// 7. 비회원 - 포트폴리오 선택하기
//    사용자가 포트폴리오 선택하기 버튼을 클릭해
//    user_portfolios 테이블에서 해당 user_id가 존재하는지 확인
//    유저가 존재한다면, 포트폴리오를 바꾸겠습니까? 응답
//    유저가 존재하지 않는다면 포트폴리오를 변경

    // 전달 받은 유저가 포트폴리오를 가지고 있는지 조회
    public boolean checkUserPortfolioExists(Integer userId) {
        return userPortfoliosRepository.existsByUserId(userId);
    }

    // 유저에 포트폴리오 삽입
    public void selectUserPortfolio(Integer userId, Integer portfolio_id) {
//        1. portfolio_id로 private_portfolios의 id로 행 조회
//        2. portfolio_id로 private_portfolios_fund_products의 portfolio_id에 해당 하는 행들 전부 조회
//        3. 1번에서 조회한 행 user_portfolios에 조회한 행(id 제외) + userId 저장(auto_increment)
//        4. 2번에서 조회한 행 user_portfolios_fund_products에 저장

        // 1. portfolio_id로 private_portfolios의 id로 행 조회
        Portfolio privatePortfolio = portfolioRepository.findById(portfolio_id)
                .orElseThrow(() -> new RuntimeException("포트폴리오를 찾을 수 없습니다."));

        // 2. portfolio_id로 private_portfolios_fund_products의 portfolio_id에 해당하는 행들 전부 조회
        List<PortfolioFund> privateFundProducts = portfolioFundRepository.findByIdPortfolioId(portfolio_id);

        // 3. 1번에서 조회한 행 user_portfolios에 조회한 행(id 제외) + userId 저장(auto_increment)
        UserPortfolios userPortfolios = new UserPortfolios();

        userPortfolios.setName(privatePortfolio.getName());
        userPortfolios.setInvestmentType(privatePortfolio.getInvestmentType());
        userPortfolios.setSummary(privatePortfolio.getSummary());
        userPortfolios.setMinimumSubscriptionFee(privatePortfolio.getMinimumSubscriptionFee());
        userPortfolios.setStockExposure(privatePortfolio.getStockExposure());
        userPortfolios.setUserId(userId);

        // privatePortfolio의 나머지 필드를 userPortfolio에 복사
        UserPortfolios savedUser = userPortfoliosRepository.save(userPortfolios);

        // 4. 2번에서 조회한 행 user_portfolios_fund_products에 저장
        for (PortfolioFund privateFundProduct : privateFundProducts) {
            UserPortfoliosFundProducts userFundProduct = new UserPortfoliosFundProducts();
            PortfolioFundId portfolioFundId = new PortfolioFundId( privateFundProduct.getId().getFundCode(), savedUser.getId());

            userFundProduct.setId(portfolioFundId);
            userFundProduct.setFundName(privateFundProduct.getFundName());
            userFundProduct.setFundTypeDetail(privateFundProduct.getFundTypeDetail());
            userFundProduct.setCompanyName(privateFundProduct.getCompanyName());
            userFundProduct.setWeight(privateFundProduct.getWeight());
            userFundProduct.setReturn3m(privateFundProduct.getReturn3m());

            userPortfoliosFundProductsRepository.save(userFundProduct);
        }
    }

    @Transactional
    public void changeUserPortfolio(int userId, Integer portfolio_id) {
        // 1. portfolio_id로 private_portfolios의 id로 행 조회
        Portfolio privatePortfolio = portfolioRepository.findById(portfolio_id)
                .orElseThrow(() -> new RuntimeException("포트폴리오를 찾을 수 없습니다."));

        // 2. portfolio_id로 private_portfolios_fund_products의 portfolio_id에 해당하는 행들 전부 조회
        List<PortfolioFund> privateFundProducts = portfolioFundRepository.findByIdPortfolioId(portfolio_id);

        // 3. userId로 UserPortfolios에서 userId에 해당하는 행 조회
        // 4. 해당 행의 id값으로
        //   UserPortfoliosFundProducts에서 portfolio_id와 저 id가 같은 행들 모두 삭제
        // 5. userId로 UserPortfolios 삭제
        // 3. userId로 UserPortfolios에서 userId에 해당하는 행 조회
        UserPortfolios existingUserPortfolios = userPortfoliosRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("포트폴리오를 찾을 수 없습니다."));;

        // 4. 해당 행의 id값으로 UserPortfoliosFundProducts에서 portfolio_id와 저 id가 같은 행들 모두 삭제

        userPortfoliosFundProductsRepository.deleteByIdPortfolioId(existingUserPortfolios.getId());

        // 5. userId로 UserPortfolios 삭제
        userPortfoliosRepository.deleteByUserId(userId);

        // 3. 1번에서 조회한 행 user_portfolios에 조회한 행(id 제외) + userId 저장(auto_increment)
        UserPortfolios userPortfolios = new UserPortfolios();

        userPortfolios.setName(privatePortfolio.getName());
        userPortfolios.setInvestmentType(privatePortfolio.getInvestmentType());
        userPortfolios.setSummary(privatePortfolio.getSummary());
        userPortfolios.setMinimumSubscriptionFee(privatePortfolio.getMinimumSubscriptionFee());
        userPortfolios.setStockExposure(privatePortfolio.getStockExposure());
        userPortfolios.setUserId(userId);

        // privatePortfolio의 나머지 필드를 userPortfolio에 복사
        UserPortfolios savedUser = userPortfoliosRepository.save(userPortfolios);

        // 4. 2번에서 조회한 행 user_portfolios_fund_products에 저장
        for (PortfolioFund privateFundProduct : privateFundProducts) {
            UserPortfoliosFundProducts userFundProduct = new UserPortfoliosFundProducts();
            PortfolioFundId portfolioFundId = new PortfolioFundId( privateFundProduct.getId().getFundCode(), savedUser.getId());

            userFundProduct.setId(portfolioFundId);
            userFundProduct.setFundName(privateFundProduct.getFundName());
            userFundProduct.setFundTypeDetail(privateFundProduct.getFundTypeDetail());
            userFundProduct.setCompanyName(privateFundProduct.getCompanyName());
            userFundProduct.setWeight(privateFundProduct.getWeight());
            userFundProduct.setReturn3m(privateFundProduct.getReturn3m());

            userPortfoliosFundProductsRepository.save(userFundProduct);
        }
    }

    //// 9. 마이데이터 - 포트폴리오 선택하기
    public void selectMyDataPortfolio(Integer userId, MyDataPortfolioDto myDataPortfolioDto) {

        // 1. user_portfolios에 조회한 행(id 제외) + userId 저장(auto_increment)
        UserPortfolios userPortfolios = new UserPortfolios();

        userPortfolios.setName(myDataPortfolioDto.getName());
        userPortfolios.setInvestmentType(myDataPortfolioDto.getInvestmentType());
        userPortfolios.setSummary(myDataPortfolioDto.getSummary());
        userPortfolios.setMinimumSubscriptionFee(myDataPortfolioDto.getMinimumSubscriptionFee());
        userPortfolios.setStockExposure(myDataPortfolioDto.getStockExposure());
        userPortfolios.setUserId(userId);

        // 2. user_portfolios에 저장
        UserPortfolios savedUser = userPortfoliosRepository.save(userPortfolios);

        // 3. 2번에서 조회한 행 user_portfolios_fund_products에 저장
        for (MyDataPortfolioDto.MyDataPortfolioFundDto privateFundProduct : myDataPortfolioDto.getFunds()) {
            UserPortfoliosFundProducts userFundProduct = new UserPortfoliosFundProducts();
            PortfolioFundId portfolioFundId = new PortfolioFundId( privateFundProduct.getFundCode(), savedUser.getId());

            userFundProduct.setId(portfolioFundId);
            userFundProduct.setFundName(privateFundProduct.getFundName());
            userFundProduct.setFundTypeDetail(privateFundProduct.getFundTypeDetail());
            userFundProduct.setCompanyName(privateFundProduct.getCompanyName());
            userFundProduct.setWeight(privateFundProduct.getWeight());
            userFundProduct.setReturn3m(privateFundProduct.getReturn3m());

            userPortfoliosFundProductsRepository.save(userFundProduct);
        }
    }

    @Transactional
    public void changeMyDataPortfolio(int userId, MyDataPortfolioDto myDataPortfolioDto) {

        // 3. userId로 UserPortfolios에서 userId에 해당하는 행 조회
        // 4. 해당 행의 id값으로
        //   UserPortfoliosFundProducts에서 portfolio_id와 저 id가 같은 행들 모두 삭제
        // 5. userId로 UserPortfolios 삭제
        // 3. userId로 UserPortfolios에서 userId에 해당하는 행 조회
        UserPortfolios existingUserPortfolios = userPortfoliosRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("포트폴리오를 찾을 수 없습니다."));;

        // 4. 해당 행의 id값으로 UserPortfoliosFundProducts에서 portfolio_id와 저 id가 같은 행들 모두 삭제

        userPortfoliosFundProductsRepository.deleteByIdPortfolioId(existingUserPortfolios.getId());

        // 5. userId로 UserPortfolios 삭제
        userPortfoliosRepository.deleteByUserId(userId);

        // 3. 1번에서 조회한 행 user_portfolios에 조회한 행(id 제외) + userId 저장(auto_increment)
        UserPortfolios userPortfolios = new UserPortfolios();

        userPortfolios.setName(myDataPortfolioDto.getName());
        userPortfolios.setInvestmentType(myDataPortfolioDto.getInvestmentType());
        userPortfolios.setSummary(myDataPortfolioDto.getSummary());
        userPortfolios.setMinimumSubscriptionFee(myDataPortfolioDto.getMinimumSubscriptionFee());
        userPortfolios.setStockExposure(myDataPortfolioDto.getStockExposure());
        userPortfolios.setUserId(userId);

        // privatePortfolio의 나머지 필드를 userPortfolio에 복사
        UserPortfolios savedUser = userPortfoliosRepository.save(userPortfolios);

        // 4. 2번에서 조회한 행 user_portfolios_fund_products에 저장
        for (MyDataPortfolioDto.MyDataPortfolioFundDto privateFundProduct : myDataPortfolioDto.getFunds()) {
            UserPortfoliosFundProducts userFundProduct = new UserPortfoliosFundProducts();
            PortfolioFundId portfolioFundId = new PortfolioFundId( privateFundProduct.getFundCode(), savedUser.getId());

            userFundProduct.setId(portfolioFundId);
            userFundProduct.setFundName(privateFundProduct.getFundName());
            userFundProduct.setFundTypeDetail(privateFundProduct.getFundTypeDetail());
            userFundProduct.setCompanyName(privateFundProduct.getCompanyName());
            userFundProduct.setWeight(privateFundProduct.getWeight());
            userFundProduct.setReturn3m(privateFundProduct.getReturn3m());

            userPortfoliosFundProductsRepository.save(userFundProduct);
        }
    }


    public MyDataFlaskResponseDto getMyDataPortfolios(Integer userId) {
        // 1. User 모듈에 나이 전달 받기

        // 2. Asset 모듈에 주식 거래내역, 자산, 보유주식
        // Transaction 객체 리스트 생성
        List<MyDataTransaction> transactions = Arrays.asList(
                new MyDataTransaction("2024-03-02", "buy", "000340"),
                new MyDataTransaction("2024-03-02", "sell", "000340")
        );

        // StockBalance 리스트 생성
        List<String> stockBalance = Arrays.asList("000430", "000200", "375800");

        // FundData 객체 생성
        MyDataFundData fundData = new MyDataFundData(userId, transactions, stockBalance, 24, 100000);


        MyDataFlaskResponseDto myDataFlaskResponseDto =  myDataPortfolioServiceClient.getMyDataPortfolio("application/json", fundData);
        log.info(myDataFlaskResponseDto.toString());
        return myDataFlaskResponseDto;
    }

    public List<MyDataTestDto> getMyDataPortfoliosLevelTest(MyDataFlaskLevelTest myDataFlaskLevelTest) {

        List<MyDataTestDto> myDataTestDtoList = new ArrayList<>();

        MyDataFlaskLevelTestResponseDto responseDto = myDataPortfolioServiceClient.getMyDataLevelTestPortfolio("application/json", myDataFlaskLevelTest);

        if (responseDto != null) {
            List<MyDataFlaskLevelTestResponseDto.FundGroup> fundGroups = responseDto.getResponse();

            for (MyDataFlaskLevelTestResponseDto.FundGroup fundGroup : fundGroups) {

                List<MyDataTestDto.FundDto> fundDtos = new ArrayList<>();

                System.out.println("Fund Class: " + fundGroup.getFundClass());


                for (MyDataFlaskLevelTestResponseDto.Fund fund : fundGroup.getFunds()) {
                    // 선택한 fund의 fund_code로 private_portfolios_fund_stocks에서 fund_code에 해당하는 데이터 가져오고 List<HititPortfoliosFundsStocksAndBondsResponseDto.FundStockDto> 형식으로 리스트로 저장
                    Optional<List<FundStocks>> fundProductsStocks = fundStocksRepository.findByIdFundCode(fund.getFundCode());
                    List<HititPortfoliosFundsStocksAndBondsResponseDto.FundStockDto> fundStockDtos = fundProductsStocks.orElse(List.of()).stream()
                            .map(stock -> new HititPortfoliosFundsStocksAndBondsResponseDto.FundStockDto(stock.getId().getStockName(), stock.getSize(), stock.getStyle(), stock.getWeight()))
                            .collect(Collectors.toList());

                    // 선택한 fund의 fund_code로 private_portfolios_fund_bonds에서 fund_code에 해당하는 데이터 가져오고 List<HititPortfoliosFundsStocksAndBondsResponseDto.FundBondDto> 형식으로 리스트로 저장
                    Optional<List<FundBonds>> fundProductsBonds = fundBondsRepository.findByIdFundCode(fund.getFundCode());
                    List<HititPortfoliosFundsStocksAndBondsResponseDto.FundBondDto> fundBondDtos = fundProductsBonds.orElse(List.of()).stream()
                            .map(bond -> new HititPortfoliosFundsStocksAndBondsResponseDto.FundBondDto(bond.getId().getBondName(), bond.getExpireDate(), bond.getDuration(), bond.getCredit(), bond.getWeight()))
                            .collect(Collectors.toList());

                    MyDataTestDto.FundDto fundDto = new MyDataTestDto.FundDto(
                            fund.getFundCode(),
                            fund.getFundName(),
                            fund.getFundTypeDetail(),
                            fund.getCompanyName(),
                            20.0, // weight
                            fund.getReturn3m(),
                            (float)fund.getStock(),
                            (float)fund.getStockForeign(),
                            (float)fund.getBond(),
                            (float)fund.getBondForeign(),
                            (float)fund.getInvestment(),
                            (float)fund.getEtc(),
                            fundStockDtos,
                            fundBondDtos
                    );
                    fundDtos.add(fundDto);

                    System.out.println("Fund Code: " + fund.getFundCode());
                    System.out.println("Fund Name: " + fund.getFundName());
                    System.out.println("Fund Type Detail: " + fund.getFundTypeDetail());
                    System.out.println("Company Name: " + fund.getCompanyName());
                    System.out.println("Return 3m: " + fund.getReturn3m());

                    System.out.println("Stock: " + fund.getStock());
                    System.out.println("Stock Foreign: " + fund.getStockForeign());
                    System.out.println("Bond: " + fund.getBond());
                    System.out.println("Bond Foreign: " + fund.getBondForeign());
                    System.out.println("Investment: " + fund.getInvestment());
                    System.out.println("Etc: " + fund.getEtc());


                    System.out.println("Risk Grade: " + fund.getRiskGrade());
                    System.out.println("Risk Grade Txt: " + fund.getRiskGradeTxt());
                    System.out.println("Set Amount: " + fund.getSetAmount());
                    System.out.println("Set Date: " + fund.getSetDate());
                    System.out.println("Std Price: " + fund.getStdPrice());

                    System.out.println("Stock Ratio: " + fund.getStockRatio());
                    System.out.println("Bond Ratio: " + fund.getBondRatio());
                    System.out.println("---------------------------------");
                }

                MyDataTestDto myDataTestDto = new MyDataTestDto(
                        "스마트세이버",
                        fundGroup.getFundClass(),
                        "이 포트폴리오는 어떤 포트폴리오입니다.",
                        100, // minimumSubscriptionFee
                        20, // stockExposure
                        30.0, // return3m
                        fundDtos
                );

                myDataTestDtoList.add(myDataTestDto);
            }
        }
        return myDataTestDtoList;
    }


    //// 리밸런싱 로직
    public OptimizeResponseCamelCaseDto optimizePortfolio() {
            // 1. user_portfolios에서 모든 데이터를 가져온다.
            List<UserPortfolios> allPortfolios = userPortfoliosRepository.findAll();

            // 2. 가져온 행의 포트폴리오 Id로 펀드 리스트를 가져온다.
            List<UserPortfoliosFundProducts> fundProducts = userPortfoliosFundProductsRepository.findByIdPortfolioId(allPortfolios.get(1).getId());

            // 3. 가져온 펀드 리스트들을 weight 기준으로 내림차순 정렬한다.
            Collections.sort(fundProducts, Comparator.comparing(UserPortfoliosFundProducts::getWeight).reversed());

            // 4. 가져온 펀드 리스트의 펀드 코드와 비중을 정렬한다.
            List<FundProductDto> fundProductDtoList = new ArrayList<>();

            for (UserPortfoliosFundProducts fundProduct : fundProducts) {
                FundProductDto dto = new FundProductDto(fundProduct.getId().getFundCode(), fundProduct.getWeight()/100);
                fundProductDtoList.add(dto);
            }

            // TODO: User id가 이건지 확인
            int userId = allPortfolios.get(0).getUserId();
            OptimizeDto optimizeDto = new OptimizeDto(userId, fundProductDtoList);

            OptimizeResponseDto response = optimizeServiceClient.getOptimizeResult("application/json", optimizeDto);

            // 만약에 리밸런싱이 되었을 경우 -> 리밸런싱 리포트를 유저에게 전달하여야 한다.
            // TODO: 리밸런싱 리포트 DB에 저장
            int rebalancingId = 1;
            // 알림 전송
            NotificationDto notificationDto = new NotificationDto(userId, rebalancingId, false, "포트폴리오를 조정했어요!");
            // 일단 있다고 가정하고 출력.
            // 리밸런싱 리포트
            // 1. 기존 펀드 리스트의 펀드 이름과 펀드 코드, 비중을 출력
            System.out.println("기존 펀드 리스트:");
            for (UserPortfoliosFundProducts fundProduct : fundProducts) {
                System.out.println("펀드 이름" + fundProduct.getFundName() + "펀드 코드" + fundProduct.getId().getFundCode() + ", 비중: " + fundProduct.getWeight());
            }

            List<Float> updatedWeights = response.getResponse().getWeights();

            System.out.println("변경된 펀드 리스트:");
            for (int i = 0; i < updatedWeights.size(); i++) {
                if (i < fundProducts.size()) {
                    String fundCode = fundProducts.get(i).getId().getFundCode();
                    Integer portfolioId = fundProducts.get(i).getId().getPortfolioId();

                    // 해당 fundCode와 portfolioId에 맞는 행을 찾습니다.
                    UserPortfoliosFundProducts fundProduct = userPortfoliosFundProductsRepository.findByIdPortfolioIdAndIdFundCode(portfolioId, fundCode);

                    // weight 값을 업데이트합니다.
                    fundProduct.setWeight(updatedWeights.get(i) * 100);
//                    userPortfoliosFundProductsRepository.save(fundProduct);

                    // 변경된 펀드 리스트 출력
                    System.out.println("펀드 이름" + fundProduct.getFundName() +  "펀드 코드: " + fundProduct.getId().getFundCode() + ", 비중: " + fundProduct.getWeight());
                }
            }
            // 2. 비중이 변경된 이후 펀드 리스트의 펀드 이름과 펀드 코드, 비중을 출력



            return OptimizeResponseMapper.toCamelCase(response);
//        for (UserPortfolios portfolio : allPortfolios) {
//            List<UserPortfoliosFundProducts> fundProducts = userPortfoliosFundProductsRepository.findByIdPortfolioId(portfolio.getId());
//
//            List<FundProductDto> fundProductDtoList = new ArrayList<>();
//
//            for (UserPortfoliosFundProducts fundProduct : fundProducts) {
//                FundProductDto dto = new FundProductDto(fundProduct.getId().getFundCode(), fundProduct.getWeight());
//                fundProductDtoList.add(dto);
//            }
//
//            OptimizeDto optimizeDto = new OptimizeDto(portfolio.getUserId(), fundProductDtoList);
//
//
//            return optimizeServiceClient.getOptimizeResult("application/json", optimizeDto);
//        }

    }

    //// Test: Spring - Flask 연동 테스트
    public String analyzeSentiment(String text) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("text", text);

        return flaskTestServiceClient.getSentiment("application/json", requestBody);
    }

    public void sendNotificationMessage(NotificationDto notificationDto) {

        log.info("send Notification Message ~~ ");
        log.info("user id : " + notificationDto.getUserId());
        log.info("relalncing id : " + notificationDto.getRebalancingId());
        log.info("check : " + notificationDto.getChecked());
        log.info("summary : " + notificationDto.getSummary());

        messageService.sendNotificationMsgToUserService(notificationDto);
    }


}