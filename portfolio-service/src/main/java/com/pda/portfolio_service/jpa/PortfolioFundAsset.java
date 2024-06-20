package com.pda.portfolio_service.jpa;

import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
@Getter
@Entity
@NoArgsConstructor
@Table(name = "private_portfolios_fund_assets")
public class PortfolioFundAsset {
    @Id
    @Column(name = "fund_code")
    private String fundCode;

    @Column(name = "stock")
    private Float stock;

    @Column(name = "stock_foreign")
    private Float stockForeign;

    @Column(name = "bond")
    private Float bond;

    @Column(name = "bond_foreign")
    private Float bondForeign;

    @Column(name = "investment")
    private Float investment;

    @Column(name = "etc")
    private Float etc;
}
