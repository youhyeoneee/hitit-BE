package com.pda.auth.controller;

import com.pda.user_service.jpa.User;
import com.pda.auth.service.AuthService;
import com.pda.user_service.service.UserService;
import com.pda.utils.api_utils.ApiUtils;
import com.pda.utils.auth.dto.AuthUserDto;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


import static com.pda.utils.api_utils.ApiUtils.error;
import static com.pda.utils.api_utils.ApiUtils.success;
@RestController
@AllArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final UserService userService;

    @GetMapping("/validate-token")
    public ApiUtils.ApiResult validateToken(@RequestHeader("Authorization") String bearerToken) {
        int userId = authService.bearerToken2UserId(bearerToken);

        try {
            User user = userService.findUserById(userId);
            AuthUserDto authUserDto = authService.convertUser2AuthUserDto(user);
            return success(authUserDto);
        } catch (ExpiredJwtException e) {
            return error("만료된 토큰입니다.", HttpStatus.BAD_REQUEST);
        } catch (MalformedJwtException | IllegalArgumentException e) {
            return error("잘못된 토큰입니다.", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return error("서버 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
