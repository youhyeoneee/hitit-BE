package com.pda.user_service.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponseDto {
    String message;
    String token;
    UserInfoDto userInfo;
}
