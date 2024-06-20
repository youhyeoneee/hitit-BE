package com.pda.portfolio_service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;


@FeignClient(name = "flaskClient", url = "http://172.31.13.91:8080/sentiment:8080/sentiment")
public interface PortfolioServiceClient {

    @PostMapping
    String getSentiment(@RequestHeader("Content-Type") String contentType, @RequestBody Map<String, Object> data);
}
