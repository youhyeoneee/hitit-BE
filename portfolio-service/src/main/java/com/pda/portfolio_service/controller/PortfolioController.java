package com.pda.portfolio_service.controller;

import com.pda.portfolio_service.dto.*;
import com.pda.portfolio_service.dto_test.MyDataTestDto;
import com.pda.portfolio_service.feign.MyDataFlaskResponseDto;
import com.pda.portfolio_service.redis.RebalancingData;
import com.pda.portfolio_service.service.PortfolioService;
import com.pda.utils.api_utils.ApiUtils;
import com.pda.utils.api_utils.CustomStringUtils;
import com.pda.utils.security.JwtTokenProvider;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.regex.Pattern;

import static com.pda.utils.api_utils.ApiUtils.error;
import static com.pda.utils.api_utils.ApiUtils.success;



@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("/api/portfolios")
@PropertySource(value = {"env.properties"})
public class PortfolioController {
    @Autowired
    private PortfolioService portfolioService;

    private final JwtTokenProvider jwtTokenProvider;

    //// 1. 자체 서비스 - 포트폴리오 전체 조회
    @GetMapping("/hitit")
    public ApiUtils.ApiResult<List<HititPortfoliosResponseDto>> getHititPortfolios() {
        // 여기까지지의 데이터는 미리 정해진 데이터
        List<HititPortfoliosResponseDto> hititPortfoliosResponseDto = portfolioService.getHititPortfolios();
        // 수익률의 경우에는 포트폴리오에 포함된 각 펀드의 수익률을 연산하여 Dto에 반환
        return success(hititPortfoliosResponseDto);
    }

    //// 2. 자체 서비스 - 포트폴리오 내 펀드 조회
    @GetMapping("/hitit/{portfolio_id}")
    public ApiUtils.ApiResult getHititPortfoliosFunds(@PathVariable("portfolio_id") Integer portfolio_id) {
        if(!isNumber(portfolio_id)){
            return error("포트폴리오 id가 전달되지 않음", HttpStatus.BAD_REQUEST);
        }
        List<HititPortfoliosFundsResponseDto> hititPortfoliosFundsResponseDto = portfolioService.getHititPortfoliosFunds(portfolio_id);

        return success(hititPortfoliosFundsResponseDto);
    }

    //// 3. 자체 서비스 - 포트폴리오 내 펀드 내 주식, 채권 조회
    @GetMapping("/hitit/{portfolio_id}/{fund_id}")
    public ApiUtils.ApiResult getHititPortfoliosFundsStocksAndBonds(@PathVariable("portfolio_id") Integer portfolio_id,
                                                     @PathVariable("fund_id") Integer fund_id) {
        if (portfolio_id == null) {
            return error("포트폴리오 id가 전달되지 않음", HttpStatus.BAD_REQUEST);
        }

        if (fund_id == null) {
            return error("펀드 id가 전달되지 않음", HttpStatus.BAD_REQUEST);
        }

        HititPortfoliosFundsStocksAndBondsResponseDto hititPortfoliosFundsStocksAndBondsResponseDto = portfolioService.getHititPortfoliosFundsStocksAndBonds(portfolio_id, fund_id);

        return success(hititPortfoliosFundsStocksAndBondsResponseDto);
    }


    //// 4. 자산 - 내 포트폴리오 조회
    @GetMapping("/user")
    public ApiUtils.ApiResult getUserPortfolioFundAssets(@RequestHeader("Authorization") String bearerToken) {
        int userId = jwtTokenProvider.bearerToken2UserId(bearerToken);
        log.info("user id : " + userId);

        PortfolioFundAssetResponseDto portfolioFundAssetResponseDto = portfolioService.getUserPortfolioFundAssets(userId);
        return success(portfolioFundAssetResponseDto);
    }

    //// 5. 자산 - 내 포트폴리오 내 펀드 조회
    @GetMapping("/userfunds")
    public ApiUtils.ApiResult getUserPortfolioFundProducts(@RequestHeader("Authorization") String bearerToken) {
        int userId = jwtTokenProvider.bearerToken2UserId(bearerToken);
        log.info("user id : " + userId);

        List<HititPortfoliosFundsResponseDto> portfolioFundAssetResponseDto = portfolioService.getUserPortfolioFundProducts(userId);
        return success(portfolioFundAssetResponseDto);
    }

    //// 6. 자산 - 내 포트폴리오 내 펀드 내 주식, 채권 조회
    @GetMapping("/userfunds/detail/{fundIdx}")
    public ApiUtils.ApiResult getUserPortfolioFundStocksAndBonds(@RequestHeader("Authorization") String bearerToken,
                                                                 @PathVariable("fundIdx") Integer fundIdx) {
        int userId = jwtTokenProvider.bearerToken2UserId(bearerToken);
        log.info("user id : " + userId);

        HititPortfoliosFundsStocksAndBondsResponseDto portfolioFundAssetResponseDto = portfolioService.getUserPortfolioFundStocksAndBonds(userId, fundIdx);
        return success(portfolioFundAssetResponseDto);
    }

    //// 7. 자체서비스 - 포트폴리오 선택하기
//    사용자가 포트폴리오 선택하기 버튼을 클릭해
//    user_portfolios 테이블에서 해당 user_id가 존재하는지 확인
//    유저가 존재한다면, 포트폴리오를 바꾸겠습니까? 응답
//    유저가 존재하지 않는다면 포트폴리오를 변경
//    @PostMapping("/select/{portfolio_id}")
//    public ApiUtils.ApiResult selectHitItPortfolio(@RequestHeader("Authorization") String bearerToken, @PathVariable("portfolio_id") Integer portfolio_id) {
//        String token = CustomStringUtils.getToken(bearerToken);
//        int userId = Integer.parseInt(jwtTokenProvider.getUsername(token));
//        log.info("user id : " + userId);
//
//        boolean exists = portfolioService.checkUserPortfolioExists(userId);
//        if (exists) {
//            return success( "포트폴리오를 바꾸겠습니까?");
//        } else {
//            portfolioService.selectUserPortfolio(userId, portfolio_id);
//            return success("포트폴리오가 변경되었습니다.");
//        }
//    }

    //// 8. 자체서비스 - 포트폴리오 선택 후 변경하기
    @PostMapping("/change/{portfolio_id}")
    public ApiUtils.ApiResult changeHitItPortfolio(@RequestHeader("Authorization") String bearerToken, @PathVariable("portfolio_id") Integer portfolio_id) {
        int userId = jwtTokenProvider.bearerToken2UserId(bearerToken);
        log.info("user id : " + userId);

        boolean exists = portfolioService.checkUserPortfolioExists(userId);
        if (exists) {
            portfolioService.changeUserPortfolio(userId, portfolio_id);
            return success("포트폴리오가 변경되었습니다.");
        } else {
            portfolioService.selectUserPortfolio(userId, portfolio_id);
            return success("포트폴리오가 선택되었습니다.");
        }
    }

    //// 9. 마이데이터 - 포트폴리오 선택하기
//    @PostMapping("/mydata/select")
//    public ApiUtils.ApiResult selectMyDataPortfolio(@RequestHeader("Authorization") String bearerToken, @RequestBody MyDataPortfolioDto myDataPortfolioDto) {
//        String token = CustomStringUtils.getToken(bearerToken);
//        int userId = Integer.parseInt(jwtTokenProvider.getUsername(token));
//        log.info("user id" + userId);
//
//        boolean exists = portfolioService.checkUserPortfolioExists(userId);
//        if (exists) {
//            return success( "포트폴리오를 바꾸겠습니까?");
//        } else {
//            portfolioService.selectMyDataPortfolio(userId, myDataPortfolioDto);
//            return success("포트폴리오가 변경되었습니다.");
//        }
//    }


    //// 10. 마이데이터 - 포트폴리오 선택하기
    @PostMapping("/mydata/change")
    public ApiUtils.ApiResult changeMyDataPortfolio(@RequestHeader("Authorization") String bearerToken, @RequestBody MyDataPortfolioDto myDataPortfolioDto) {
        int userId = jwtTokenProvider.bearerToken2UserId(bearerToken);
        log.info("user id : " + userId);

        boolean exists = portfolioService.checkUserPortfolioExists(userId);
        if (exists) {
            portfolioService.changeMyDataPortfolio(userId, myDataPortfolioDto);
            return success("포트폴리오가 변경되었습니다.");
        } else {
            portfolioService.selectMyDataPortfolio(userId, myDataPortfolioDto);
            return success("포트폴리오가 선택되었습니다.");
        }
    }


    // 11. 마이데이터 - 포트폴리오 추천 받기
    //// 이걸로 진행할 것
    //// 1. jwt token으로 user 꺼내기
    @GetMapping("/mydata")
    public ApiUtils.ApiResult<List<MyDataTestDto>> getMyDataPortfolios(@RequestHeader("Authorization") String bearerToken) {
        int userId = jwtTokenProvider.bearerToken2UserId(bearerToken);
        log.info("user id : " + userId);

        // 1. OpenFeign으로 User에게 user에게 나이, 투자성향테스트 레벨 가져오기

        // 2. OpenFeign으로 Asset에게 투자거래내역, 보유주식, 총자산 가져오기

        List<MyDataTestDto> myDataPortfoliosResponseDto = portfolioService.getMyDataPortfolios(userId);
        return success(myDataPortfoliosResponseDto);
    }





//    @PostMapping("/mydata/leveltest")
//    public ApiUtils.ApiResult<List<MyDataTestDto>> getMyDataPortfoliosLevelTest(@RequestBody MyDataFlaskLevelTest myDataFlaskLevelTest) {
//        List<MyDataTestDto> myDataFlaskLevelTestResponseDto = portfolioService.getMyDataPortfoliosLevelTest(myDataFlaskLevelTest);
//
//        return success(myDataFlaskLevelTestResponseDto);
//    }

    @GetMapping("/rebal/getweight")
    public ApiUtils.ApiResult<RebalancingData> optimizePortfolio() {

        return success(portfolioService.optimizePortfolio());
    }

    //// Test: Spring - Flask 연동 테스트
    @GetMapping("/analyze-sentiment/{text}")
    public String analyzeSentiment(@PathVariable("text") String text) {
        return portfolioService.analyzeSentiment(text);
    }

    // 나중에 util - Validator로 이동
    public static boolean isNumber(int num) {
        return Pattern.matches("^[0-9]*$", Integer.toString(num));
    }
}