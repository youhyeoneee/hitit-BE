package com.pda.asset_service.jpa;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "security_stocks")
public class SecurityStock {

    @Id
    private Integer id;

    @Column(name = "account_no")
    private String accountNo;

    @Column(name = "stock_code")
    private String stockCode;
}

