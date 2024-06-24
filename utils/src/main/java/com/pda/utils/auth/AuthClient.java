package com.pda.utils.auth;


import com.pda.utils.auth.dto.AuthUserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;


@FeignClient(name = "user-service", url = "http://localhost:8081")
public interface AuthClient {
    @GetMapping("/validate-token")
   AuthUserDto validateToken(@RequestHeader("Authorization") String bearerToken);

}



