package com.pda.portfolio_service.jpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "user_portfolios_fund_products")
public class UserPortfoliosFundProducts {
    @EmbeddedId
    private PortfolioFundId id;

    @Column(name = "fund_name")
    private String fundName;

    @Column(name = "fund_type_detail")
    private String fundTypeDetail;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "weight")
    private Float weight;

    @Column(name = "return_3m")
    private Float return3m;
}

