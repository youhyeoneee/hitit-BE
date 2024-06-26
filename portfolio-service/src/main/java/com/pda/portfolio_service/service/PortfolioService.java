package com.pda.portfolio_service.service;

import com.pda.portfolio_service.dto.*;
import com.pda.portfolio_service.dto_test.MyDataTestDto;
import com.pda.portfolio_service.feign.*;
import com.pda.portfolio_service.jpa.*;
import com.pda.portfolio_service.redis.RebalancingData;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
    private FundPricesRepository fundPricesRepository;

    @Autowired
    private FlaskTestServiceClient flaskTestServiceClient;

    @Autowired
    private MyDataPortfolioServiceClient myDataPortfolioServiceClient;

    @Autowired
    private OptimizeServiceClient optimizeServiceClient;

    private final MessageService messageService;
    private final UserServiceClient userServiceClient;

    private final DataService dataService;

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
                .map(stock -> {
                    String stockName = stock.getId().getStockName();
                    StockIncomeRevResponseDto.StockIncomeRevDto stockIncomeRevDto = stockName2IncomeRev(stockName);
                    return new HititPortfoliosFundsStocksAndBondsResponseDto.FundStockDto(stockName, stock.getSize(), stock.getStyle(), stock.getWeight(), stockIncomeRevDto.getIncome(), stockIncomeRevDto.getRev());})
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
        log.info("user ID : " + userId);
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
    @Transactional
    public HititPortfoliosFundsStocksAndBondsResponseDto getUserPortfolioFundStocksAndBonds(Integer userId, Integer fundId) {
        int retryCount = 0;
        final int maxRetries = 10;


        while (retryCount < maxRetries) {
            try {
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
                                -> new NoSuchElementException("펀드가 존재하지 않습니다."));
                ;

                // 선택한 fund의 fund_code로 private_portfolios_fund_stocks에서 fund_code에 해당하는 데이터 가져오고 List<HititPortfoliosFundsStocksAndBondsResponseDto.FundStockDto> 형식으로 리스트로 저장
                Optional<List<FundStocks>> fundProductsStocks = fundStocksRepository.findByIdFundCode(selectedFund.getId().getFundCode());

                // 주식
                List<HititPortfoliosFundsStocksAndBondsResponseDto.FundStockDto> fundStockDtos = fundProductsStocks.orElse(List.of()).stream()
                        .map(stock -> {
                            String stockName = stock.getId().getStockName();
                            StockIncomeRevResponseDto.StockIncomeRevDto stockIncomeRevDto = stockName2IncomeRev(stockName);
                            return new HititPortfoliosFundsStocksAndBondsResponseDto.FundStockDto(stockName, stock.getSize(), stock.getStyle(), stock.getWeight(), stockIncomeRevDto.getIncome(), stockIncomeRevDto.getRev());})
                        .collect(Collectors.toList());

                // 선택한 fund의 fund_code로 private_portfolios_fund_stocks에서 fund_code에 해당하는 데이터 가져오고 List<HititPortfoliosFundsStocksAndBondsResponseDto.FundStockDto> 형식으로 리스트로 저장
//                Optional<List<FundStocks>> fundProductsStocks = fundStocksRepository.findByIdFundCode(selectedFund.getId().getFundCode());
//                List<HititPortfoliosFundsStocksAndBondsResponseDto.FundStockDto> fundStockDtos = fundProductsStocks.orElse(List.of()).stream()
//                        .map(stock -> new HititPortfoliosFundsStocksAndBondsResponseDto.FundStockDto(stock.getId().getStockName(), stock.getSize(), stock.getStyle(), stock.getWeight()))
//                        .collect(Collectors.toList());

                // 선택한 fund의 fund_code로 private_portfolios_fund_bonds에서 fund_code에 해당하는 데이터 가져오고 List<HititPortfoliosFundsStocksAndBondsResponseDto.FundBondDto> 형식으로 리스트로 저장
                Optional<List<FundBonds>> fundProductsBonds = fundBondsRepository.findByIdFundCode(selectedFund.getId().getFundCode());
                List<HititPortfoliosFundsStocksAndBondsResponseDto.FundBondDto> fundBondDtos = fundProductsBonds.orElse(List.of()).stream()
                        .map(bond -> new HititPortfoliosFundsStocksAndBondsResponseDto.FundBondDto(bond.getId().getBondName(), bond.getExpireDate(), bond.getDuration(), bond.getCredit(), bond.getWeight()))
                        .collect(Collectors.toList());

                // 모든 데이터를 저장할 HititPortfoliosFundsStocksAndBondsResponseDto 생성 후 담아서 Controller로 전달
                HititPortfoliosFundsStocksAndBondsResponseDto response = new HititPortfoliosFundsStocksAndBondsResponseDto(
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

                return response;
            } catch (NoSuchElementException e) {
                retryCount++;
                if (retryCount >= maxRetries) {
                    throw new RuntimeException("최대 재시도 횟수에 도달했습니다.", e);
                }
            }
        }
        throw new RuntimeException("알 수 없는 오류가 발생했습니다.");
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
        // 현재 날짜를 LocalDate로 가져오기
        LocalDate currentDate = LocalDate.now();

        // 원하는 형식으로 날짜를 포맷팅
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = currentDate.format(formatter);

        // 포맷된 문자열을 Date 객체로 변환
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = simpleDateFormat.parse(formattedDate);
            userPortfolios.setCreatedAt(date);
            System.out.println("Formatted Date: " + formattedDate);
            System.out.println("Date 객체: " + date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

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
        // 현재 날짜를 LocalDate로 가져오기
        LocalDate currentDate = LocalDate.now();

        // 원하는 형식으로 날짜를 포맷팅
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = currentDate.format(formatter);

        // 포맷된 문자열을 Date 객체로 변환
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = simpleDateFormat.parse(formattedDate);
            userPortfolios.setCreatedAt(date);
            System.out.println("Formatted Date: " + formattedDate);
            System.out.println("Date 객체: " + date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

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
        // 현재 날짜를 LocalDate로 가져오기
        LocalDate currentDate = LocalDate.now();

        // 원하는 형식으로 날짜를 포맷팅
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = currentDate.format(formatter);

        // 포맷된 문자열을 Date 객체로 변환
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = simpleDateFormat.parse(formattedDate);
            userPortfolios.setCreatedAt(date);
            System.out.println("Formatted Date: " + formattedDate);
            System.out.println("Date 객체: " + date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

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
        // 현재 날짜를 LocalDate로 가져오기
        LocalDate currentDate = LocalDate.now();

        // 원하는 형식으로 날짜를 포맷팅
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = currentDate.format(formatter);

        // 포맷된 문자열을 Date 객체로 변환
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = simpleDateFormat.parse(formattedDate);
            userPortfolios.setCreatedAt(date);
            System.out.println("Formatted Date: " + formattedDate);
            System.out.println("Date 객체: " + date);
        } catch (ParseException e) {
            e.printStackTrace();
        }


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


    public List<MyDataTestDto> getMyDataPortfolios(Integer userId) {
        // 1. User 모듈에 나이 전달 받기
        UserAgeTestScoreDto userAgeTestScoreDto = userServiceClient.getUserAgeTestScore(userId);
        Integer age = userAgeTestScoreDto.getAge();
        Integer userTestScore = userAgeTestScoreDto.getUserTestScore();
        log.info("user age : " + age + " and test score : " + userTestScore);

        // 2. Asset 모듈에 주식 거래내역, 자산, 보유주식
        // Transaction 객체 리스트 생성
//        List<MyDataTransaction> transactions = Arrays.asList(
//                new MyDataTransaction("2024-03-02", "buy", "000340"),
//                new MyDataTransaction("2024-03-02", "sell", "000340")
//        );

        // StockBalance 리스트 생성
//        List<String> stockBalance = Arrays.asList("000430", "000200", "375800");

        MydataDto mydata = dataService.getUserMydataByOpenFeign(userId);
        // 중복을 제거한 일차원 배열 생성
        Set<String> uniqueStockCodes = new HashSet<>();
        for (SecurityAccountStocksDto dto : mydata.getSecurityStocks()) {
            uniqueStockCodes.addAll(dto.getStockCodes());
        }

        // Set을 List로 변환
        List<String> uniqueStockCodesList = new ArrayList<>(uniqueStockCodes);


        System.out.println(mydata.getSecurityTransactions());

        // 변환된 MyDataTransaction 리스트 생성
        // 현재 날짜와 시간을 가져옵니다.
        Date date = new Date();

        // 날짜 형식을 지정합니다.
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        // Date 객체를 String으로 변환합니다.
        String strDate = formatter.format(date);

        List<MyDataTransaction> transactions = mydata.getSecurityTransactions().stream()
                .flatMap(dto -> dto.getSecurityAccountTransactions().stream())
                .map(tx -> new MyDataTransaction(formatter.format(tx.getTxDatetime()),tx.getTxType(), tx.getStockCode()))
                .collect(Collectors.toList());

        // 결과 출력
        transactions.forEach(System.out::println);

        // FundData 객체 생성
        MyDataFundData fundData = new MyDataFundData(userId, transactions, uniqueStockCodesList, age, mydata.getTotalAssets());

        MyDataFlaskResponseDto myDataFlaskResponseDto = new MyDataFlaskResponseDto();

        if (userTestScore != null) {
            log.info("user age : " + age + " and test score : " + userTestScore);
            fundData.setUser_test_score(userTestScore);
            myDataFlaskResponseDto =  myDataPortfolioServiceClient.getMyDataPortfolio("application/json", fundData);
        } else {
            log.warn("user age : " + age + " but test score is null");
            myDataFlaskResponseDto =  myDataPortfolioServiceClient.getMyDataPortfolio("application/json", fundData);

        }

        List<MyDataTestDto> myDataTestDtoList = new ArrayList<>();

        if (myDataFlaskResponseDto != null) {
            List<MyDataFlaskResponseDto.FundGroup> fundGroups = myDataFlaskResponseDto.getResponse();

            for (MyDataFlaskResponseDto.FundGroup fundGroup : fundGroups) {

                List<MyDataTestDto.FundDto> fundDtos = new ArrayList<>();

                System.out.println("Fund Class: " + fundGroup.getFundClass());

                int count = 0;
                for (MyDataFlaskResponseDto.Fund fund : fundGroup.getFunds()) {
                    // 선택한 fund의 fund_code로 private_portfolios_fund_stocks에서 fund_code에 해당하는 데이터 가져오고 List<HititPortfoliosFundsStocksAndBondsResponseDto.FundStockDto> 형식으로 리스트로 저장
                    Optional<List<FundStocks>> fundProductsStocks = fundStocksRepository.findByIdFundCode(fund.getFundCode());
                    List<HititPortfoliosFundsStocksAndBondsResponseDto.FundStockDto> fundStockDtos = fundProductsStocks.orElse(List.of()).stream()
                            .map(stock -> {
                                String stockName = stock.getId().getStockName();
                                log.info(">>>> search : " + stockName);
                                StockIncomeRevResponseDto.StockIncomeRevDto stockIncomeRevDto = stockName2IncomeRev(stockName);
                                return new HititPortfoliosFundsStocksAndBondsResponseDto.FundStockDto(stockName, stock.getSize(), stock.getStyle(), stock.getWeight(), stockIncomeRevDto.getIncome(), stockIncomeRevDto.getRev());})
                            .collect(Collectors.toList());

                    // 선택한 fund의 fund_code로 private_portfolios_fund_bonds에서 fund_code에 해당하는 데이터 가져오고 List<HititPortfoliosFundsStocksAndBondsResponseDto.FundBondDto> 형식으로 리스트로 저장
                    Optional<List<FundBonds>> fundProductsBonds = fundBondsRepository.findByIdFundCode(fund.getFundCode());
                    List<HititPortfoliosFundsStocksAndBondsResponseDto.FundBondDto> fundBondDtos = fundProductsBonds.orElse(List.of()).stream()
                            .map(bond -> new HititPortfoliosFundsStocksAndBondsResponseDto.FundBondDto(bond.getId().getBondName(), bond.getExpireDate(), bond.getDuration(), bond.getCredit(), bond.getWeight()))
                            .collect(Collectors.toList());

                    double weight;
                    switch (count) {
                        case 0:
                            weight = 30.0;
                            break;
                        case 1:
                        case 2:
                            weight = 20.0;
                            break;
                        case 3:
                        case 4:
                            weight = 15.0;
                            break;
                        default:
                            weight = 0.0;
                            break;
                    }
                    count++;

                    MyDataTestDto.FundDto fundDto = new MyDataTestDto.FundDto(
                            fund.getFundCode(),
                            fund.getFundName(),
                            fund.getFundTypeDetail(),
                            fund.getCompanyName(),
                            weight, // weight
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

                double return3m = 0.0;
                for(MyDataTestDto.FundDto fundDto : fundDtos) {
                    return3m = return3m + fundDto.getReturn3m()*(fundDto.getWeight()/100);
                }

                MyDataTestDto myDataTestDto = new MyDataTestDto(
                        "HitIt 개인화 상품",
                        fundGroup.getFundClass(),
                        "이 포트폴리오는 사용자의 개인화된 정보를 통해 추천된 상품입니다.",
                        100, // minimumSubscriptionFee
                        20, // stockExposure
                        return3m, // return3m
                        fundDtos
                );

                myDataTestDtoList.add(myDataTestDto);
            }
        }
        return myDataTestDtoList;
    }

//    public List<MyDataTestDto> getMyDataPortfoliosLevelTest(MyDataFlaskLevelTest myDataFlaskLevelTest) {
//
//        List<MyDataTestDto> myDataTestDtoList = new ArrayList<>();
//
//        MyDataFlaskLevelTestResponseDto responseDto = myDataPortfolioServiceClient.getMyDataLevelTestPortfolio("application/json", myDataFlaskLevelTest);
//
//        if (responseDto != null) {
//            List<MyDataFlaskLevelTestResponseDto.FundGroup> fundGroups = responseDto.getResponse();
//
//            for (MyDataFlaskLevelTestResponseDto.FundGroup fundGroup : fundGroups) {
//
//                List<MyDataTestDto.FundDto> fundDtos = new ArrayList<>();
//
//                System.out.println("Fund Class: " + fundGroup.getFundClass());
//
//
//                for (MyDataFlaskLevelTestResponseDto.Fund fund : fundGroup.getFunds()) {
//                    // 선택한 fund의 fund_code로 private_portfolios_fund_stocks에서 fund_code에 해당하는 데이터 가져오고 List<HititPortfoliosFundsStocksAndBondsResponseDto.FundStockDto> 형식으로 리스트로 저장
//                    Optional<List<FundStocks>> fundProductsStocks = fundStocksRepository.findByIdFundCode(fund.getFundCode());
//                    List<HititPortfoliosFundsStocksAndBondsResponseDto.FundStockDto> fundStockDtos = fundProductsStocks.orElse(List.of()).stream()
//                            .map(stock -> {
//                                String stockName = stock.getId().getStockName();
//                                log.info(">>>> search : " + stockName);
//                                StockIncomeRevResponseDto.StockIncomeRevDto stockIncomeRevDto = stockName2IncomeRev(stockName);
//                                return new HititPortfoliosFundsStocksAndBondsResponseDto.FundStockDto(stockName, stock.getSize(), stock.getStyle(), stock.getWeight(), stockIncomeRevDto.getIncome(), stockIncomeRevDto.getRev());})
//                            .collect(Collectors.toList());
//
//                    // 선택한 fund의 fund_code로 private_portfolios_fund_bonds에서 fund_code에 해당하는 데이터 가져오고 List<HititPortfoliosFundsStocksAndBondsResponseDto.FundBondDto> 형식으로 리스트로 저장
//                    Optional<List<FundBonds>> fundProductsBonds = fundBondsRepository.findByIdFundCode(fund.getFundCode());
//                    List<HititPortfoliosFundsStocksAndBondsResponseDto.FundBondDto> fundBondDtos = fundProductsBonds.orElse(List.of()).stream()
//                            .map(bond -> new HititPortfoliosFundsStocksAndBondsResponseDto.FundBondDto(bond.getId().getBondName(), bond.getExpireDate(), bond.getDuration(), bond.getCredit(), bond.getWeight()))
//                            .collect(Collectors.toList());
//
//                    MyDataTestDto.FundDto fundDto = new MyDataTestDto.FundDto(
//                            fund.getFundCode(),
//                            fund.getFundName(),
//                            fund.getFundTypeDetail(),
//                            fund.getCompanyName(),
//                            20.0, // weight
//                            fund.getReturn3m(),
//                            (float)fund.getStock(),
//                            (float)fund.getStockForeign(),
//                            (float)fund.getBond(),
//                            (float)fund.getBondForeign(),
//                            (float)fund.getInvestment(),
//                            (float)fund.getEtc(),
//                            fundStockDtos,
//                            fundBondDtos
//                    );
//                    fundDtos.add(fundDto);
//
//                    System.out.println("Fund Code: " + fund.getFundCode());
//                    System.out.println("Fund Name: " + fund.getFundName());
//                    System.out.println("Fund Type Detail: " + fund.getFundTypeDetail());
//                    System.out.println("Company Name: " + fund.getCompanyName());
//                    System.out.println("Return 3m: " + fund.getReturn3m());
//
//                    System.out.println("Stock: " + fund.getStock());
//                    System.out.println("Stock Foreign: " + fund.getStockForeign());
//                    System.out.println("Bond: " + fund.getBond());
//                    System.out.println("Bond Foreign: " + fund.getBondForeign());
//                    System.out.println("Investment: " + fund.getInvestment());
//                    System.out.println("Etc: " + fund.getEtc());
//
//
//                    System.out.println("Risk Grade: " + fund.getRiskGrade());
//                    System.out.println("Risk Grade Txt: " + fund.getRiskGradeTxt());
//                    System.out.println("Set Amount: " + fund.getSetAmount());
//                    System.out.println("Set Date: " + fund.getSetDate());
//                    System.out.println("Std Price: " + fund.getStdPrice());
//
//                    System.out.println("Stock Ratio: " + fund.getStockRatio());
//                    System.out.println("Bond Ratio: " + fund.getBondRatio());
//                    System.out.println("---------------------------------");
//                }
//
//                MyDataTestDto myDataTestDto = new MyDataTestDto(
//                        "스마트세이버",
//                        fundGroup.getFundClass(),
//                        "이 포트폴리오는 어떤 포트폴리오입니다.",
//                        100, // minimumSubscriptionFee
//                        20, // stockExposure
//                        30.0, // return3m
//                        fundDtos
//                );
//
//                myDataTestDtoList.add(myDataTestDto);
//            }
//        }
//        return myDataTestDtoList;
//    }


    //// 리밸런싱 로직
    public OptimizeResponseDto optimizePortfolio(int userId) {
            // 1. user_portfolios에서 모든 데이터를 가져온다.
            UserPortfolios allPortfolios = userPortfoliosRepository.findByUserId(userId)
                    .orElseThrow(() -> new RuntimeException("펀드를 찾을 수 없습니다."));;

            // 2. 가져온 행의 포트폴리오 Id로 펀드 리스트를 가져온다.
            List<UserPortfoliosFundProducts> fundProducts = userPortfoliosFundProductsRepository.findByIdPortfolioId(allPortfolios.getId());

            // 3. fundProducts 리스트를 "국내채권형", "해외채권형"으로 분리
            List<UserPortfoliosFundProducts> domesticAndOverseasBondFunds = new ArrayList<>();
            List<UserPortfoliosFundProducts> otherFunds = new ArrayList<>();

            for (UserPortfoliosFundProducts fundProduct : fundProducts) {
                if ("국내채권형".equals(fundProduct.getFundTypeDetail()) || "해외채권형".equals(fundProduct.getFundTypeDetail())) {
                    domesticAndOverseasBondFunds.add(fundProduct);
                } else {
                otherFunds.add(fundProduct);
                }
            }

            // 4. "국내채권형", "해외채권형"이 아닌 항목들을 weight 기준으로 내림차순 정렬
            Collections.sort(otherFunds, Comparator.comparing(UserPortfoliosFundProducts::getWeight).reversed());

            // 5. 정렬된 리스트에 "국내채권형", "해외채권형" 항목들을 뒤에 추가
            otherFunds.addAll(domesticAndOverseasBondFunds);

            // 6. "국내채권형", "해외채권형"이 아닌 항목들의 개수를 셈
            int countNonBondFunds = otherFunds.size() - domesticAndOverseasBondFunds.size();

            // 결과 출력 (필요시)
            System.out.println("국내채권형, 해외채권형이 아닌 펀드들의 갯수: " + countNonBondFunds);

            // 정렬된 리스트
            List<UserPortfoliosFundProducts> sortedFundProducts = otherFunds;

            // "해외주식형" 펀드의 인덱스 리스트를 구함
            List<Integer> overseasIndexes = new ArrayList<>();
            for (int i = 0; i < sortedFundProducts.size(); i++) {
                if ("해외주식형".equals(sortedFundProducts.get(i).getFundTypeDetail())) {
                    overseasIndexes.add(i);
                }
            }


            // 4. 가져온 펀드 리스트의 펀드 코드와 비중을 정렬한다.
            List<FundProductDto> fundProductDtoList = new ArrayList<>();
            List<Float> beforeWeight = new ArrayList<>();

            for (int i = 0; i < sortedFundProducts.size(); i++) {
                beforeWeight.add(sortedFundProducts.get(i).getWeight()/100);
            }

            for (UserPortfoliosFundProducts fundProduct : sortedFundProducts) {
                FundProductDto dto = new FundProductDto(fundProduct.getId().getFundCode(), fundProduct.getWeight()/100);
                fundProductDtoList.add(dto);
            }


//            // TODO: User id가 이건지 확인
//            int userId = allPortfolios.get(4).getUserId();
            OptimizeDto optimizeDto = new OptimizeDto(userId, countNonBondFunds, overseasIndexes, fundProductDtoList);

            OptimizeResponseDto response = optimizeServiceClient.getOptimizeResult("application/json", optimizeDto);

            OptimizeResponseDto responseOptimize = enrichOptimizeResponse(response);
            responseOptimize.getResponse().setBeforeWeights(beforeWeight);


//            // 만약에 리밸런싱이 되었을 경우 -> 리밸런싱 리포트를 유저에게 전달하여야 한다.
//            // TODO: 리밸런싱 리포트 DB에 저장
//            int rebalancingId = 1;
//            // 알림 전송
//            NotificationDto notificationDto = new NotificationDto(userId, rebalancingId, false, "포트폴리오를 조정했어요!");
//            // 일단 있다고 가정하고 출력.
//            // 리밸런싱 리포트
//            // 1. 기존 펀드 리스트의 펀드 이름과 펀드 코드, 비중을 출력
//            System.out.println("기존 펀드 리스트:");
//            for (UserPortfoliosFundProducts fundProduct : sortedFundProducts) {
//                System.out.println("펀드 이름" + fundProduct.getFundName() + "펀드 코드" + fundProduct.getId().getFundCode() + ", 비중: " + fundProduct.getWeight());
//            }


            // 리밸런싱 포트폴리오
//            RebalancingData rebalancingData = new RebalancingData();
//
//
//            // 전 Weight
//            List<Float> beforeWeight = new ArrayList<>();
//
//            for (int i = 0; i < sortedFundProducts.size(); i++) {
//                beforeWeight.add(sortedFundProducts.get(i).getWeight());
//            }
//
//            // 후 Weight
//            List<Float> afterWeight = new ArrayList<>();
//
//            for (int i = 0; i < response.getResponse().getWeights().size(); i++) {
//                float weight = response.getResponse().getWeights().get(i) * 100;
//                float roundedWeight = Math.round(weight * 10) / 10.0f;
//                afterWeight.add(roundedWeight);
//            }
//
//
//
//            // VarianceData 초기화
//            // 이거 넣어줘야함
//            List<RebalancingData.VarianceData> variance = new ArrayList<>();
//
//            for (int i = 0; i < sortedFundProducts.size(); i++) {
//                RebalancingData.WeightData weightData = new RebalancingData.WeightData();
//                weightData.setAfterWeights(afterWeight.get(i));
//                weightData.setBeforeWeights(beforeWeight.get(i));
//
//                List<RebalancingData.WeightData> listWeightData = new ArrayList<>();
//                listWeightData.add(weightData);
//
//                RebalancingData.VarianceData varianceData = new RebalancingData.VarianceData();
//                Map<String, List<RebalancingData.WeightData>> varianceMap = new HashMap<>();
//                varianceMap.put(sortedFundProducts.get(i).getFundName(), listWeightData);
//                varianceData.setVariance(varianceMap);
//
//                variance.add(varianceData);
//            }
//
//        // variance, userId 초기화
//        rebalancingData.setVariance(variance);
//        rebalancingData.setUserId(response.getResponse().getUserId());
//
//        List<RebalancingData.FundData> rebalancingDatas = new ArrayList<>();
//
//
//        for(int i = 0; i < response.getResponse().getFunds().size(); i++) {
//
//            // 펀드 1개의 펀드 코드
//            FundProducts rebalanceFunds =  fundProductsRepository.findById( response.getResponse().getFunds().get(i).getFundCode())
//                    .orElseThrow(() -> new RuntimeException("펀드를 찾을 수 없습니다."));
//
//            RebalancingData.FundInfo fundInfo = new RebalancingData.FundInfo();
//            fundInfo.setFundName( rebalanceFunds.getFundName());
//            fundInfo.setCompanyName(rebalanceFunds.getCompanyName());
//            fundInfo.setReturn3m(rebalanceFunds.getReturn3m());
//
//
//            List<String> stockName = new ArrayList<>();
//            List<String> badNewsTitles = new ArrayList<>();
//            List<String> badNewsUrls = new ArrayList<>();
//            List<String> stockCodes = new ArrayList<>();
//            List<String> rev = new ArrayList<>();;
//            List<String> income = new ArrayList<>();
//
//            List<RebalancingData.StockInfo> stockInfoList = new ArrayList<>();
//            for(int j = 0; j < response.getResponse().getFunds().get(i).getStocks().size(); j++) {
//                stockName.add(response.getResponse().getFunds().get(i).getStocks().get(j).getStockName());
//                badNewsTitles.add(response.getResponse().getFunds().get(i).getStocks().get(j).getBadNewsTitle());
//                badNewsUrls.add(response.getResponse().getFunds().get(i).getStocks().get(j).getBadNewsUrl());
//                stockCodes.add(response.getResponse().getFunds().get(i).getStocks().get(j).getStockCode());
//                rev.add(response.getResponse().getFunds().get(i).getStocks().get(j).getRev());
//                income.add(response.getResponse().getFunds().get(i).getStocks().get(j).getIncome());
//            }
//
//            RebalancingData.StockInfo stockInfo = new RebalancingData.StockInfo();
//            stockInfo.setStockName(stockName);
//            stockInfo.setBadNewsTitles(badNewsTitles);
//            stockInfo.setBadNewsUrls(badNewsUrls);
//            stockInfo.setStockCodes(stockCodes);
//            stockInfo.setRev(rev);
//            stockInfo.setIncome(income);
//
//            stockInfoList.add(stockInfo);
//
//            fundInfo.setStockInfo(stockInfoList);
//
//            List<RebalancingData.FundInfo> fundInfoList = new ArrayList<>();
//            fundInfoList.add(fundInfo);
//
//            Map<String, List<RebalancingData.FundInfo>> fundData = new HashMap<>();
//            fundData.put(response.getResponse().getFunds().get(i).getFundCode(), fundInfoList);
//
//            RebalancingData.FundData fundDatas = new RebalancingData.FundData();
//            fundDatas.setFundData(fundData);
//
//            rebalancingDatas.add(fundDatas);
//        }
//
//        rebalancingData.setRebalancingData(rebalancingDatas);


        List<Float> updatedWeights = response.getResponse().getWeights();

            System.out.println("변경된 펀드 리스트:");
            for (int i = 0; i < updatedWeights.size(); i++) {
                if (i < sortedFundProducts.size()) {
                    String fundCode = sortedFundProducts.get(i).getId().getFundCode();
                    Integer portfolioId = sortedFundProducts.get(i).getId().getPortfolioId();

                    // 해당 fundCode와 portfolioId에 맞는 행을 찾습니다.
                    UserPortfoliosFundProducts fundProduct = userPortfoliosFundProductsRepository.findByIdPortfolioIdAndIdFundCode(portfolioId, fundCode);

                    // weight 값을 업데이트합니다.
                    fundProduct.setWeight(updatedWeights.get(i) * 100);
//                    userPortfoliosFundProductsRepository.save(fundProduct);

                    // 변경된 펀드 리스트 출력
                    System.out.println("펀드 이름" + fundProduct.getFundName() +  "펀드 코드: " + fundProduct.getId().getFundCode() + ", 비중: " + fundProduct.getWeight());
                }
            }


//            return OptimizeResponseMapper.toCamelCase(response);
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
        return responseOptimize;
    }

    public List<Map<Date, Float>> getMyDataPortfoliosRate(int userId) {
        log.info("user ID : " + userId);
        // 1. User의 Portfolio Id 가져오기
        UserPortfolios userPortfolios
                = userPortfoliosRepository.findByUserId(userId)
                .orElseThrow(()
                        -> new NoSuchElementException("유저가 존재하지 않습니다."));
        // 2. 해당 포트폴리오가 생성된 날짜를 조회
        Date createdAt = userPortfolios.getCreatedAt();

         // 3. Portfolio Id를 통해 펀드리스트 가져오기
        List<UserPortfoliosFundProducts> fundProducts = userPortfoliosFundProductsRepository.findByIdPortfolioId(userPortfolios.getId());


        // 각 펀드의 코드와 비중을 매핑
        Map<String, Float> fundCodeToWeightMap = fundProducts.stream()
                .collect(Collectors.toMap(fp -> fp.getId().getFundCode(), UserPortfoliosFundProducts::getWeight));

        // 펀드 코드별 가격 리스트 저장
        Map<String, List<FundPrices>> fundPricesMap = new HashMap<>();

        for (String fundCode : fundCodeToWeightMap.keySet()) {
            List<FundPrices> fundPrices = fundPricesRepository.findByIdFundCodeAndIdDateGreaterThanEqual(fundCode, createdAt);
            fundPricesMap.put(fundCode, fundPrices);
        }

        // 일자별 가격 저장할 리스트 생성
        List<Map<Date, Float>> fundDates = new ArrayList<>();

        // 날짜별로 가격 계산
        if (!fundPricesMap.isEmpty()) {
            int maxSize = fundPricesMap.values().stream().mapToInt(List::size).max().orElse(0);

            for (int i = 0; i < maxSize; i++) {
                Map<Date, Float> dailyPricesMap = new HashMap<>();
                float totalWeightedPrice = 0f;
                Date date = null;

                for (String fundCode : fundPricesMap.keySet()) {
                    List<FundPrices> fundPrices = fundPricesMap.get(fundCode);
                    if (fundPrices != null && i < fundPrices.size()) {
                        FundPrices fundPrice = fundPrices.get(i);
                        date = fundPrice.getId().getDate();  // 날짜는 동일한 인덱스의 날짜를 사용
                        float weightedPrice = fundPrice.getPrice() * (fundCodeToWeightMap.get(fundCode) / 100);
                        totalWeightedPrice += weightedPrice;
                    }
                }

                if (date != null) {
                    dailyPricesMap.put(date, totalWeightedPrice);
                    fundDates.add(dailyPricesMap);
                }
            }
        }

        return fundDates;
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

    private StockIncomeRevResponseDto.StockIncomeRevDto stockName2IncomeRev(String stockName) {
        log.info("stock name : " + stockName);
        StockRevIncomeRequestDto stockRevIncomeRequestDto = new StockRevIncomeRequestDto(stockName);
        StockIncomeRevResponseDto stockIncomeRevResponseDto = myDataPortfolioServiceClient.getStockIncomeRev("application/json", stockRevIncomeRequestDto);
        StockIncomeRevResponseDto.StockIncomeRevDto result = stockIncomeRevResponseDto.getResponse();
        if (result == null) {
            return new StockIncomeRevResponseDto.StockIncomeRevDto(null, null);
        }
        return result;
    }




    public OptimizeResponseDto enrichOptimizeResponse(OptimizeResponseDto optimizeResponseDto) {
        OptimizeResponseDto.Response response = optimizeResponseDto.getResponse();

        List<OptimizeResponseDto.Fund> enrichedFunds = response.getFunds().stream()
                .map(this::enrichFund)
                .collect(Collectors.toList());

        OptimizeResponseDto.Response enrichedResponse = new OptimizeResponseDto.Response(
                enrichedFunds,
                response.getUserId(),
                null,
                response.getWeights()
        );

        return new OptimizeResponseDto(enrichedResponse);
    }

    private OptimizeResponseDto.Fund enrichFund(OptimizeResponseDto.Fund fund) {
        // JpaRepository를 사용하여 추가 정보를 가져옴
        FundProducts fundEntity = fundProductsRepository.findById(fund.getFundCode())
                .orElseThrow(() -> new RuntimeException("Fund not found with code: " + fund.getFundCode()));

        fund.setFundName(fundEntity.getFundName());
        fund.setCompanyName(fundEntity.getCompanyName());
        fund.setReturn3m(fundEntity.getReturn3m());

        List<OptimizeResponseDto.Stock> enrichedStocks = fund.getStocks().stream()
                .map(this::enrichStock)
                .collect(Collectors.toList());

        fund.setStocks(enrichedStocks);

        return fund;
    }

    private OptimizeResponseDto.Stock enrichStock(OptimizeResponseDto.Stock stock) {
        // 현재 Stock 객체에 추가적인 정보가 필요하지 않으므로 그대로 반환
        return stock;
    }
}