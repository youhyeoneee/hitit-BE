package com.pda.portfolio_service.jpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "private_portfolios")
public class Portfolio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String investmentType;
    private String summary;
    private Integer minimumSubscriptionFee;
    private Integer stockExposure;


    public Portfolio(String name, String investmentType, String summary, Integer minimumSubscriptionFee, Integer stockExposure) {
        this.name = name;
        this.investmentType = investmentType;
        this.summary = summary;
        this.minimumSubscriptionFee = minimumSubscriptionFee;
        this.stockExposure = stockExposure;
    }
}

