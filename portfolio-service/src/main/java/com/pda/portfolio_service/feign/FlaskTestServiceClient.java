package com.pda.portfolio_service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.PropertySource;

import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@PropertySource(value = {"env.properties"})
@FeignClient(name = "flaskClient", url = "http://172.31.16.127:8080")
public interface FlaskTestServiceClient {
    @PostMapping("/sentiment")
    String getSentiment(@RequestHeader("Accept") String contentType, @RequestBody Map<String, Object> data);
}