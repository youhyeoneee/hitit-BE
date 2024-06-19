package com.pda.portfolio_service.jpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "private_portfolios_fund_products")
public class PortfolioFund {
    @EmbeddedId
    private HititPortfoliosFundProductId id;

    @Column(name = "fund_name")
    private String fundName;

    @Column(name = "fund_type_detail")
    private String fundTypeDetail;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "weight")
    private Float weight;
}
