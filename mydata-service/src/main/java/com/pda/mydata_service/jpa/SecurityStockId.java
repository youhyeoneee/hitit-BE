package com.pda.mydata_service.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SecurityStockId implements Serializable {
    @ManyToOne
    @JoinColumn(name = "account_no")
    private SecurityAccount securityAccount;

    @Column(name = "stock_code")
    private String stockCode;
}

