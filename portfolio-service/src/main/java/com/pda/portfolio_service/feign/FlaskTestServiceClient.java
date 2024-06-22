package com.pda.portfolio_service.feign;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@PropertySource(value = {"env.properties"})
@FeignClient(name = "flaskClient", url = "${ml.client.url}")
public interface PortfolioServiceClient {
    @PostMapping("/sentiment")
    String getSentiment(@RequestHeader("Accept") String contentType, @RequestBody Map<String, Object> data);
}