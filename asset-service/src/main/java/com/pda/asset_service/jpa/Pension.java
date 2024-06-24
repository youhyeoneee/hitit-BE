package com.pda.asset_service.jpa;


import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "pensions")
public class Pension implements Serializable {

    @EmbeddedId
    private PensionId pensionId;

    @Column(name = "pension_type")
    private String pensionType;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "user_id")
    private int userId;

    @Column(name = "interest_rate")
    private Double interestRate;

    @Column(name = "evaluation_amount")
    private String evaluationAmount;

    @Column(name = "expiration_date")
    private Date expirationDate;

    @Column(name = "retirement_pension_claimed")
    private Boolean retirementPensionClaimed;
}

