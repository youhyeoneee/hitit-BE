package com.pda.portfolio_service.controller;

import com.pda.portfolio_service.feign.MydataDto;
import com.pda.portfolio_service.service.DataService;
import com.pda.utils.api_utils.ApiUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.pda.utils.security.JwtTokenProvider;

import static com.pda.utils.api_utils.ApiUtils.success;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("/api/portfolios")
public class DataController {
    private final JwtTokenProvider jwtTokenProvider;
    private final DataService dataService;

    @GetMapping("/mydata-asset")
    public ApiUtils.ApiResult<MydataDto> getUserMydata(@RequestHeader("Authorization") String bearerToken) {
        int userId = jwtTokenProvider.bearerToken2UserId(bearerToken);
        log.info("user id : " + userId);

        MydataDto mydata = dataService.getUserMydataByOpenFeign(userId);
        return success(mydata);

    }
}
