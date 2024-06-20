package com.pda.portfolio_service.jpa;

import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
@Getter
@Entity
@NoArgsConstructor
@Table(name = "private_portfolios_fund_stocks")
public class PortfolioFundStock {
    @EmbeddedId
    private PortfolioFundStockId id;

    @Column(name = "size")
    private String size;

    @Column(name = "style")
    private String style;

    @Column(name = "weight")
    private Float weight;
}
