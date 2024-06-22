package com.pda.user_service.dto;

import com.pda.user_service.jpa.User;
import lombok.Getter;

@Getter
public class UserInfoDto {
    String name;
    String gender;
    String type;
    String mydata;
    Boolean portfolio;

    public UserInfoDto(User user) {
        this.name = user.getName();
        this.gender = user.getGender();
        this.type = user.getInvestmentType();
        this.mydata = user.getMydata();
        this.portfolio = user.getPortfolio();
    }
}
