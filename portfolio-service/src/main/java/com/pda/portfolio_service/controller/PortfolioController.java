package com.pda.portfolio_service.controller;

import com.pda.portfolio_service.dto.HititPortfoliosResponseDto;
import com.pda.portfolio_service.service.PortfolioService;
import com.pda.utils.api_utils.ApiUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.pda.utils.api_utils.ApiUtils.error;
import static com.pda.utils.api_utils.ApiUtils.success;

@RestController
@Slf4j
@RequestMapping("/api/portfolios")
public class PortfolioController {
    @Autowired
    private PortfolioService portfolioService;

    @GetMapping("/hitit")
    public ApiUtils.ApiResult<List<HititPortfoliosResponseDto>> getHititPortfolios() {
        List<HititPortfoliosResponseDto> hititPortfoliosResponseDto = portfolioService.getHititPortfolios();
        return success(hititPortfoliosResponseDto);
    }

    @GetMapping("/hitit/{portfolio_id}")
    public ApiUtils.ApiResult getHititPortfoliosFund(@PathVariable("portfolio_id") String portfolio_id) {
        if(portfolio_id.isBlank()){
            return error("포트폴리오 id가 전달되지 않음", HttpStatus.BAD_REQUEST);
        }
        List<HititPortfoliosFundsResponseDto> hititPortfoliosFundsResponseDto = portfolioService.getHititPortfolios(portfolio_id);
        return success(hititPortfoliosFundsResponseDto);
    }
}