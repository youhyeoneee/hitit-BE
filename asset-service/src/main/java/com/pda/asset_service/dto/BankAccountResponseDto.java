package com.pda.asset_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BankAccountResponseDto {
    private String accountNo;
    private String bankName;
    private String accountType;
    private String name;
    private int balance;
    private Date createdAt;
    private int userId;
}
