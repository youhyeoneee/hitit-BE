package com.pda.portfolio_service.jpa;

import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

import java.util.Date;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "fund_bonds")
public class FundBonds {
    @EmbeddedId
    private PortfolioFundBondId id;

    @Column(name = "expire_date")
    private Date expireDate;

    @Column(name = "duration")
    private Float duration;

    @Column(name = "credit")
    private String credit;

    @Column(name = "weight")
    private Float weight;
}