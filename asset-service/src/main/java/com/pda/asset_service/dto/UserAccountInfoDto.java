package com.pda.asset_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pda.asset_service.jpa.BankAccount;
import lombok.Getter;

import java.util.List;

@Getter
public class UserAccountInfoDto {
    @JsonProperty("user_id")
    private int userId;

    @JsonProperty("bank_accounts")
    private List<String> bankAccounts;

    @JsonProperty("security_accounts")
    private List<String> securityAccounts;

    private List<String> cards;

    private List<String> pensions;

    private List<String> loans;

    private List<String> funds;


}
