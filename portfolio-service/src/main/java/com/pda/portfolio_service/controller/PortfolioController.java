package com.pda.portfolio_service.controller;

import com.pda.portfolio_service.service.PortfolioService;
import com.pda.security.JwtTokenProvider;
import com.pda.user_service.dto.LoginDto;
import com.pda.user_service.dto.LoginResponseDto;
import com.pda.user_service.dto.SignupUserDto;
import com.pda.user_service.dto.UserInfoDto;
import com.pda.user_service.jpa.User;
import com.pda.user_service.service.UserService;
import com.pda.utils.api_utils.ApiUtils;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static com.pda.utils.api_utils.ApiUtils.error;
import static com.pda.utils.api_utils.ApiUtils.success;

@RestController
@Slf4j
@RequestMapping("/api/portfolios")
@PropertySource(value = {"env.properties"})
public class PortfolioController {
    @Autowired
    private PortfolioService portfolioService;

    @GetMapping("/hitit")
    public ApiUtils.ApiResult getHititPortfolio() {
         HititResponseDto hititResponseDto = portfolioService.getHititPortfolio();
        return success(HititResponseDto);
    }
}
