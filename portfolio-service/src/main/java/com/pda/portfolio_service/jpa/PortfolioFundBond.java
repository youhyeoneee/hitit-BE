package com.pda.portfolio_service.jpa;

import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

import java.util.Date;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "private_portfolios_fund_bonds")
public class PortfolioFundBond {
    @EmbeddedId
    private PortfolioFundBondId id;

    @Column(name = "expired_date")
    private Date expiredDate;

    @Column(name = "duration")
    private Float duration;

    @Column(name = "credit")
    private String credit;

    @Column(name = "weight")
    private Float weight;
}