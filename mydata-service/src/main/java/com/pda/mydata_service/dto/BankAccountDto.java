package com.pda.mydata_service.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class BankAccountDto {
//    @JsonProperty("account_no")
    private String accountNo;

//    @JsonProperty("bank_name")
    private String bankName;

//    @JsonProperty("account_type")
    private String accountType;

    private String name;

    private int balance;

//    @JsonProperty("created_at")
    private Date createdAt;

//    @JsonProperty("user_id")
    private int userId;
}
