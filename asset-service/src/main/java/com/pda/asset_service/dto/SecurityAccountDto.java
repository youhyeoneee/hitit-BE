package com.pda.asset_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SecurityAccountDto {
    @JsonProperty("account_no")
    private String accountNo;

    @JsonProperty("security_name")
    private String securityName;

    @JsonProperty("account_type")
    private String accountType;

    private int balance;

    @JsonProperty("created_at")
    private Date createdAt;

    @JsonProperty("user_id")
    private int userId;
}
