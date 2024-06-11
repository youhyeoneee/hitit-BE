package com.pda.user_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pda.user_service.jpa.User;
import com.pda.utils.api_utils.StringUtils;
import lombok.Getter;

@Getter
public class UserDto {
    private String email;
    private String name;
    private String password;
    private String phone;
    @JsonProperty("resident_number")
    private String residentNumber;

    public User toEntity() {
        String birthdate = StringUtils.getBirthDate(residentNumber);
        String gender = StringUtils.getGender(residentNumber);
        return new User(email, name, password, phone, birthdate, gender);
    }
}
