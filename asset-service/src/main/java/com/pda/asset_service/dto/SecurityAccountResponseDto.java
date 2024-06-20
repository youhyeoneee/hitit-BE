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
public class SecurityAccountResponseDto {
    private String accountNo;

    private String securityName;

    private String accountType;

    private Integer balance;

    private Date createdAt;

    private int userId;
}
