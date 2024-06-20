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
public class CardDto {
    @JsonProperty("card_no")
    private String cardNo;

    @JsonProperty("company_name")
    private String companyName;

    @JsonProperty("card_name")
    private String cardName;

    @JsonProperty("card_type")
    private String cardType;

    @JsonProperty("created_at")
    private Date createdAt;

    @JsonProperty("expired_at")
    private Date expiredAt;

    @JsonProperty("account_no")
    private String accountNo;

    @JsonProperty("user_id")
    private int userId;
}
