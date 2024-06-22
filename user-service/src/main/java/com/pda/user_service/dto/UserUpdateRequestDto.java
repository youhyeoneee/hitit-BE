package com.pda.user_service.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserUpdateRequestDto {
    private String email;
    private String name;
    private String password;
    private String phone;
    private String birthdate;
    private String gender;
    private String investmentType;
    private Integer investmentTestScore;
    private String mydata;
    private Boolean portfolio;
}