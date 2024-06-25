package com.pda.utils.security.openfeign;


import com.pda.utils.security.dto.UserDetailsDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", url = "http://localhost:8081")
public interface AuthClient {
    @GetMapping("/api/openfeign/users/validate/{user_id}")
    UserDetailsDto validateUser(@PathVariable("user_id") int userId);
}



