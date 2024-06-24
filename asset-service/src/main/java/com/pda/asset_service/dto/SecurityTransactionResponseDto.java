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
public class SecurityTransactionResponseDto {
    private Integer id;

    private Date txDatetime;

    private String txType;

    private String txAmount;

    private Integer balAfterTx;

    private Integer txQty;

    private String accountNo;

    private String stockCode;
}
