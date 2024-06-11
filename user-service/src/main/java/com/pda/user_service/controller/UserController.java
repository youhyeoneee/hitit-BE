package com.pda.user_service.controller;


import com.pda.user_service.dto.UserDto;
import com.pda.user_service.jpa.User;
import com.pda.user_service.service.UserService;
import com.pda.utils.api_utils.ApiUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/signup")
    public ApiUtils.ApiResult<String> signup(@RequestBody UserDto userDto) {
        User savedUser = userService.signup(userDto);
        return success("회원가입 성공");
    }

    @GetMapping("/login/kakao/code")
    public String kakaoLogin(@RequestParam String code) {
        String token = userService.getAccessTokenFromKakao(code);
        return userService.getUserInfo(token);
    }

}
