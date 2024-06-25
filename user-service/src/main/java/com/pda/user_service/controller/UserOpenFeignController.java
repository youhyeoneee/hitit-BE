package com.pda.user_service.controller;

import com.pda.user_service.dto.UserAgeTestScoreDto;
import com.pda.user_service.jpa.User;
import com.pda.user_service.service.UserService;
import com.pda.utils.api_utils.CustomStringUtils;
import com.pda.utils.security.JwtTokenProvider;
import com.pda.utils.security.dto.UserDetailsDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/users/openfeign")
public class UserOpenFeignController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping("/validate/{user_id}")
    public UserDetailsDto validateUser(@PathVariable("user_id") int userId) {
        User user = userService.findUserById(userId);
        return userService.convert2UserDetailsDto(user);
    }

    @GetMapping("/{user_id}")
    public UserAgeTestScoreDto getAgeTestScoreByUserId(@PathVariable("user_id") int userId) {
        User user = userService.findUserById(userId);
        int age = CustomStringUtils.calculateAge(user.getBirthdate());
        return new UserAgeTestScoreDto(age, user.getInvestmentTestScore());
    }
}
