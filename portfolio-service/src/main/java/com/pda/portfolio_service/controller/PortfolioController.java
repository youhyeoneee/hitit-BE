package com.pda.portfolio_service.controller;

import com.pda.portfolio_service.dto.HititResponseDto;
import com.pda.portfolio_service.service.PortfolioService;
import com.pda.utils.api_utils.ApiUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.pda.utils.api_utils.ApiUtils.success;

@RestController
@Slf4j
@RequestMapping("/api/portfolios")
public class PortfolioController {
    @Autowired
    private PortfolioService portfolioService;

    @GetMapping("/hitit")
    public ApiUtils.ApiResult<List<HititResponseDto>> getHititPortfolios() {
        List<HititResponseDto> hititResponseDto = portfolioService.getHititPortfolios();
        return success(hititResponseDto);
    }
}