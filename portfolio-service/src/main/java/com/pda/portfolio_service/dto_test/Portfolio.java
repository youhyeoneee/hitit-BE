package com.pda.portfolio_service.dto_test;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Portfolio {
    private int id;
    private String name;
    private String investmentType;
    private String summary;
    private int minimumSubscriptionFee;
    private int stockExposure;
    private double return3m;
    private List<Fund> funds;
}

