package com.pda.portfolio_service.controller;

import com.pda.portfolio_service.dto.*;
import com.pda.portfolio_service.service.PortfolioService;
import com.pda.security.JwtTokenProvider;
import com.pda.utils.api_utils.ApiUtils;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
public class PortfolioController {
    @Autowired
    private PortfolioService portfolioService;

    @GetMapping("/hitit")
    public ApiUtils.ApiResult<List<HititPortfoliosResponseDto>> getHititPortfolios() {
        // 여기까지지의 데이터는 미리 정해진 데이터
        List<HititPortfoliosResponseDto> hititPortfoliosResponseDto = portfolioService.getHititPortfolios();
        // 수익률의 경우에는 포트폴리오에 포함된 각 펀드의 수익률을 연산하여 Dto에 반환
        return success(hititPortfoliosResponseDto);
    }

    @GetMapping("/hitit/{portfolio_id}")
    public ApiUtils.ApiResult getHititPortfoliosFunds(@PathVariable("portfolio_id") Integer portfolio_id) {
        if(!isNumber(portfolio_id)){
            return error("포트폴리오 id가 전달되지 않음", HttpStatus.BAD_REQUEST);
        }
        List<HititPortfoliosFundsResponseDto> hititPortfoliosFundsResponseDto = portfolioService.getHititPortfoliosFunds(portfolio_id);

        return success(hititPortfoliosFundsResponseDto);
    }

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

    @PostMapping("/user")
    public ApiUtils.ApiResult getUserPortfolioFundAssets(@RequestBody UserPortfolioFundRequestDto userPortfolioFundRequestDto) {
        PortfolioFundAssetResponseDto portfolioFundAssetResponseDto = portfolioService.getUserPortfolioFundAssets(userPortfolioFundRequestDto);
        return success(portfolioFundAssetResponseDto);
    }

    // 나중에 util - Validator로 이동
    public static boolean isNumber(int num) {
        return Pattern.matches("^[0-9]*$", Integer.toString(num));
    }
}