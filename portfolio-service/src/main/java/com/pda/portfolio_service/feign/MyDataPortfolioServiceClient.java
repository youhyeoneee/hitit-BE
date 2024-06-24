package com.pda.portfolio_service.feign;

import com.pda.portfolio_service.dto.MyDataFlaskLevelTest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
@FeignClient(name = "mydataPortfolioClient", url = "http://172.31.13.91:8080")
public interface MyDataPortfolioServiceClient {
    @PostMapping("/mydata/funds")
    MyDataFlaskResponseDto getMyDataPortfolio(@RequestHeader("Accept") String contentType,
                                                    @RequestBody MyDataFundData myDataFundData);

    @PostMapping("/mydata/fundss")
    MyDataFlaskLevelTestResponseDto getMyDataLevelTestPortfolio(@RequestHeader("Accept") String contentType,
                                                                @RequestBody MyDataFlaskLevelTest myDataFlaskLevelTest);
}