package com.pda.auth.service;

import com.pda.user_service.jpa.User;
import com.pda.utils.api_utils.CustomStringUtils;
import com.pda.auth.security.JwtTokenProvider;
import com.pda.utils.auth.dto.AuthUserDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@AllArgsConstructor
@Service
public class AuthService {
    private final JwtTokenProvider jwtTokenProvider;

    public int bearerToken2UserId(String bearerToken) throws IllegalArgumentException {

        try {
            // 토큰 -> user id
            log.info("bearerToken : " + bearerToken);
            String token = CustomStringUtils.getToken(bearerToken);
            int userId = Integer.parseInt(jwtTokenProvider.getUsername(token));
            log.info("user id : " + userId);
            return userId;
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }


    public AuthUserDto convertUser2AuthUserDto(User user) {
        return new AuthUserDto(user.getId(),
                user.getEmail(),
                user.getName(),
                user.getPassword(),
                user.getPhone(),
                user.getBirthdate(),
                user.getGender(),
                user.getInvestmentType(),
                user.getInvestmentTestScore(),
                user.getMydata(),
                user.getPortfolio()
        );
    }
}
