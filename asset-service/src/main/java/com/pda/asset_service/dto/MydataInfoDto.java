package com.pda.asset_service.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MydataInfoDto {
    private int id;
    private int userId;
    private String assetType;
    private String companyName;
    private String accountType;
    private String accountNo;
}