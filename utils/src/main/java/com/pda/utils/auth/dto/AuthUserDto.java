package com.pda.utils.auth.dto;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
//@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AuthUserDto {
    private int id;
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
