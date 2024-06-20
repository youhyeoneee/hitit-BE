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
public class CardDto {

    private String cardNo;

    private String companyName;

    private String cardName;

    private String cardType;

    private Date createdAt;

    private Date expiredAt;

    private String accountNo;

    private int userId;
}
