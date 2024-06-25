package com.pda.asset_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pda.asset_service.jpa.SecurityAccount;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SecurityTransactionDto {

    private Integer id;

    @JsonProperty("tx_datetime")
    private Date txDatetime;

    @JsonProperty("tx_type")
    private String txType;

    @JsonProperty("tx_amount")
    private String txAmount;

    @JsonProperty("bal_after_tx")
    private Integer balAfterTx;

    @JsonProperty("tx_qty")
    private Integer txQty;

    @JsonProperty("account_no")
    private String accountNo;

    @JsonProperty("stock_code")
    private String stockCode;
}
