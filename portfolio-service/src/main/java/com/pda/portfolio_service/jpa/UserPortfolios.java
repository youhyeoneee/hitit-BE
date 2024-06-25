package com.pda.portfolio_service.jpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "user_portfolios")
public class UserPortfolios {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Integer id;

    @Column(name = "name", length = 255)
    private String name;

    @Column(name = "investment_type", length = 255)
    private String investmentType;

    @Column(name = "summary", length = 255)
    private String summary;

    @Column(name = "minimum_subscription_fee")
    private Integer minimumSubscriptionFee;

    @Column(name = "stock_exposure")
    private Integer stockExposure;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "created_at")
    private Date createdAt;
}