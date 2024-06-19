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
public class BankAccountResponseDto {

    private String accountNo;

    private String bankName;

    private String accountType;

    private String name;

    private int balance;

    private Date createdAt;

    private int userId;
}
