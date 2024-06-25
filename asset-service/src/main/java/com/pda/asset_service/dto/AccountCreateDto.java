package com.pda.asset_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class AccountCreateDto {
    private String name;
    private String ssn;
    private String password;
    private Integer balance;
}