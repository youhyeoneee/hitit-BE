package com.pda.mydata_service.dto;

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
    private String accountNo;

    private String securityName;

    private String accountType;

    private int balance;

    private Date createdAt;

    private int userId;
}
