package com.pda.asset_service.jpa;

import jakarta.persistence.*;
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
@Table(name = "pensions")
public class Pension {

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "pension_name")
    private String pensionName;

    @Column(name = "pension_type")
    private String pensionType;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private AssetUser assetUser;

    @Column(name = "interest_rate")
    private Double interestRate;

    @Column(name = "evaluation_amount")
    private String evaluationAmount;

    @Column(name = "expiration_date")
    private Date expirationDate;

    @Id
    @Column(name = "account_no")
    private String accountNo;
}
