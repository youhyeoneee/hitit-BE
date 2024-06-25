package com.pda.portfolio_service.feign;

import com.pda.portfolio_service.dto.UserAgeTestScoreDto;
import com.pda.utils.api_utils.CustomStringUtils;
import com.pda.utils.security.dto.UserDetailsDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", url = "${service.url.user}/api/openfeign/users/")
public interface MydataUserClient {

    @GetMapping("/{user_id}")
    UserAgeTestScoreDto getAgeTestScoreByUserId(@PathVariable("user_id") int userId);
}
