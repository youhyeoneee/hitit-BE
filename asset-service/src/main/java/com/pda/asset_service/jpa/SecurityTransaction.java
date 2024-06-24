package com.pda.asset_service.jpa;


import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "security_transaction")
public class SecurityTransaction {
    @Id
    private Integer id;

    @Column(name = "tx_datetime")
    private Date txDatetime;

    @Column(name = "tx_type")
    private String txType;

    @Column(name = "tx_amount")
    private String txAmount;

    @Column(name = "bal_after_tx")
    private Integer balAfterTx;

    @Column(name = "tx_qty")
    private Integer txQty;

    @ManyToOne
    @JoinColumn(name = "account_no", referencedColumnName = "account_no")
    private SecurityAccount securityAccount;

    @Column(name = "stock_code")
    private String stockCode;
}
