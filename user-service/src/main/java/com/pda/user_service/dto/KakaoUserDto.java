package com.pda.user_service.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.pda.user_service.jpa.User;
import com.pda.utils.api_utils.CustomStringUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@NoArgsConstructor
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class KakaoUserDto {
    private Long id;
    private Date connectedAt;
    private KakaoAccount kakaoAccount;

    @Getter
    @NoArgsConstructor
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    public class KakaoAccount {
        // 이름
        private Boolean nameNeedsAgreement;
        private String name;
        // 이메일
        private Boolean hasEmail;
        private Boolean emailNeedsAgreement;
        private Boolean isEmailValid;
        private Boolean isEmailVerified;
        private String email;
        // 전화번호
        private Boolean phoneNumberNeedsAgreement;
        private String phoneNumber;
        // 생년
        private Boolean birthyearNeedsAgreement;
        private String birthyear;
        // 생일
        private Boolean birthdayNeedsAgreement;
        private String birthday;
        private String birthdayType;
        // 성별
        private Boolean hasGender;
        private Boolean genderNeedsAgreement;
        private String gender;
    }

    public User toEntity(String token) {
        String birthdate = kakaoAccount.birthyear + kakaoAccount.birthday;
        String phone = CustomStringUtils.formatPhone(kakaoAccount.phoneNumber);
        return new User(kakaoAccount.email, kakaoAccount.name, token, phone, birthdate, kakaoAccount.gender);
    }
}