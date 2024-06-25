package com.pda.portfolio_service.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "fund_prices")
public class FundPrices {
    @EmbeddedId
    private FundPricesId id;

    @Column(name = "price")
    private Float price;
}