package com.pda.user_service.controller;


import com.pda.security.JwtTokenProvider;
import com.pda.user_service.dto.LoginDto;
import com.pda.user_service.dto.LoginResponseDto;
import com.pda.user_service.dto.UserInfoDto;
import com.pda.user_service.dto.SignupUserDto;
import com.pda.user_service.jpa.User;
import com.pda.user_service.service.UserService;
import com.pda.utils.api_utils.ApiUtils;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
public class UserController {

    @Value("${kakao.client_id}")
    private String clientId;
    @Value("${kakao.redirect_uri}")
    private String redirectUri;

    @Autowired
    private UserService userService;
    @Autowired
    JwtTokenProvider jwtTokenProvider;


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

        String token = jwtTokenProvider.createToken(user.getUsername());
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
        log.info("kakao user" + user.getId() + " - " + user.getUsername() + " - " + user.getEmail() );
        String token = jwtTokenProvider.createToken(user.getUsername());
        UserInfoDto userInfo = new UserInfoDto(user);
        LoginResponseDto loginResponseDto = new LoginResponseDto("로그인 성공", token, userInfo);
        return success(loginResponseDto);
    }

}