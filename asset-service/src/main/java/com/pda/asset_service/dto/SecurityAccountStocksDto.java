package com.pda.asset_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SecurityAccountStocksDto {

    private String accountNo;

    private List<String> stockCodes;
}
