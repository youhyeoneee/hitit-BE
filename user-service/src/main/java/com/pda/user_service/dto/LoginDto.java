package com.pda.user_service.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginDto {
    String message;
    String token;
}
