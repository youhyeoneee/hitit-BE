package com.pda.utils.security.openfeign;


import com.pda.utils.security.dto.UserDetailsDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@PropertySource(value = "env.properties")
@FeignClient(name = "user-service", url = "${service.url.user}")
public interface AuthClient {
    @GetMapping("/api/users/openfeign/validate/{user_id}")
    UserDetailsDto validateUser(@PathVariable("user_id") int userId);
}



