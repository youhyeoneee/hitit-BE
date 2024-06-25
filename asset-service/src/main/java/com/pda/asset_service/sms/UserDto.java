package com.pda.asset_service.sms;

import lombok.Getter;

public class UserDto {

    @Getter
    public static class SmsCertificationRequest {

        private String phone;
        private String certificationNumber;

    }
}