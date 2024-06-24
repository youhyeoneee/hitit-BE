package com.pda.asset_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pda.asset_service.jpa.SecurityAccount;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SecurityStockDto {
    @JsonProperty("account_no")
    private String accountNo;

    @JsonProperty("stock_code")
    private String stockCode;
}
