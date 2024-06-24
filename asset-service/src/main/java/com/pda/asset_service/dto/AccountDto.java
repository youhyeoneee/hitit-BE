package com.pda.asset_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountDto {
    @JsonProperty("id")
    private int id;

    @JsonProperty("user_id")
    private int userId;

    private String name;

    private String ssn;

    @JsonProperty("company_name")
    private String companyName;

    @JsonProperty("pension_type")
    private String pensionType;

    @JsonProperty("account_no")
    private String accountNo;

    private String password;

    private Integer balance;

    @JsonProperty("created_at")
    private Date createdAt;

    @JsonProperty("updated_at")
    private Date updatedAt;
}
