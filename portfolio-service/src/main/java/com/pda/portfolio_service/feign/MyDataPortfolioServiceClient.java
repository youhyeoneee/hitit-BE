package com.pda.portfolio_service.feign;

import com.pda.portfolio_service.dto.MyDataFlaskLevelTest;
import com.pda.portfolio_service.dto.StockIncomeRevResponseDto;
import com.pda.portfolio_service.dto.StockRevIncomeRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.*;


@PropertySource("env.properties")
@FeignClient(name = "mydataPortfolioClient", url = "${service.url.ml}")
public interface MyDataPortfolioServiceClient {
    @PostMapping("/mydata/funds/test")
    MyDataFlaskResponseDto getMyDataPortfolio(@RequestHeader("Accept") String contentType,
                                                    @RequestBody MyDataFundData myDataFundData);

    @PostMapping("/mydata/funds")
    MyDataFlaskResponseDto getMyDataLevelTestPortfolio(@RequestHeader("Accept") String contentType,
                                                                @RequestBody MyDataFundData myDataFundData);

    @PostMapping(value = "/dart/info", consumes = "application/json", produces = "application/json")
    StockIncomeRevResponseDto getStockIncomeRev(@RequestHeader("Accept") String contentType, @RequestBody StockRevIncomeRequestDto stockRevIncomeRequestDto);

}