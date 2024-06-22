package com.pda.portfolio_service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
@FeignClient(name = "mydataPortfolioClient", url = "${ml.server.url}")
public interface MyDataPortfolioServiceClient {
    @GetMapping("/mydata/fundsrecom/{user_id}")
    String getMyDataPortfolio(@RequestHeader("Accept") String contentType,
                              @RequestParam Map<String, Integer> params);
}