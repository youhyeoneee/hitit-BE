package com.pda.mydata_service.dto;

import com.pda.mydata_service.jpa.SecurityAccount;
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

    private Date txDatetime;

    private String txType;

    private String txAmount;

    private Integer balAfterTx;

    private Integer txQty;

    private String accountNo;

    private String stockCode;
}
