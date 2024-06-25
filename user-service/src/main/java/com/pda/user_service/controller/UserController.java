package com.pda.user_service.controller;


import com.pda.user_service.dto.*;
import com.pda.user_service.jpa.User;
import com.pda.user_service.service.UserService;
import com.pda.utils.api_utils.ApiUtils;
import com.pda.utils.security.JwtTokenProvider;
import com.pda.utils.security.dto.UserDetailsDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static com.pda.utils.api_utils.ApiUtils.error;
import static com.pda.utils.api_utils.ApiUtils.success;

@RestController
@Slf4j
@RequestMapping("/api/users")
@PropertySource(value = {"env.properties"})
@RequiredArgsConstructor
public class UserController {

    @Value("${kakao.client_id}")
    private String clientId;
    @Value("${kakao.redirect_uri}")
    private String redirectUri;
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    

    @PatchMapping()
    public ApiUtils.ApiResult updateUser(@RequestBody UserUpdateRequestDto userUpdateRequestDto, @RequestHeader("Authorization") String bearerToken) {
        // 토큰 -> user id
        log.info("bearerToken : " + bearerToken);
        int userId = jwtTokenProvider.bearerToken2UserId(bearerToken);
        log.info("user id : " + userId);

        User updatedUser = userService.updateUser(userId, userUpdateRequestDto);
        UserInfoDto userInfo = new UserInfoDto(updatedUser);
        return success(userInfo);
    }

    @PostMapping("/signup")
    public ApiUtils.ApiResult<String> signup(@Valid @RequestBody SignupUserDto userDto) {
        User savedUser = userService.signup(userDto);
        return success("회원가입 성공");
    }

    @PostMapping("/login")
    public ApiUtils.ApiResult login(@Valid @RequestBody LoginDto loginDto) {

        User user = userService.login(loginDto);

        if (user == null) {
            return error("로그인 실패", HttpStatus.BAD_REQUEST);
        }

        log.info("email user log in : " + user.getId() + " - " + user.getEmail() );
        String token = jwtTokenProvider.createToken(String.valueOf(user.getId()));
        UserInfoDto userInfo = new UserInfoDto(user);
        LoginResponseDto loginResponseDto = new LoginResponseDto("로그인 성공", token, userInfo);
        return success(loginResponseDto);
    }

    @GetMapping("/login/kakao")
    public ApiUtils.ApiResult kakaoLogin(@RequestParam String code) {
        String accessTokenFromKakao = userService.getAccessTokenFromKakao(code);
        User user = userService.getUserInfo(accessTokenFromKakao);

        if (user == null) {
            return error("로그인 실패", HttpStatus.BAD_REQUEST);
        }
        log.info("kakao user log in : " + user.getId() + " - " + user.getEmail() );
        String token = jwtTokenProvider.createToken(String.valueOf(user.getId()));
        UserInfoDto userInfo = new UserInfoDto(user);
        LoginResponseDto loginResponseDto = new LoginResponseDto("로그인 성공", token, userInfo);
        return success(loginResponseDto);
    }

}