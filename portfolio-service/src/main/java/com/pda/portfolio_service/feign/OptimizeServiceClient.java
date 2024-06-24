package com.pda.portfolio_service.feign;

import com.pda.portfolio_service.dto.MyDataFlaskLevelTest;
import com.pda.portfolio_service.dto.OptimizeDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
@FeignClient(name = "optimizeServiceClient", url = "http://172.31.13.91:8080")
public interface OptimizeServiceClient {
    @PostMapping("/rebal/getweight")
    OptimizeResponseDto getOptimizeResult(@RequestHeader("Accept") String contentType,
                                              @RequestBody OptimizeDto optimizeDto);

}