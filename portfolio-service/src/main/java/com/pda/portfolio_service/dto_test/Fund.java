package com.pda.portfolio_service.dto_test;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Fund {
    private String fundCode;
    private int portfolioId;
    private String fundName;
    private String fundTypeDetail;
    private String companyName;
    private double weight;
    private double return3m;
    private List<Stock> fundStocks;
    private List<Bond> fundBonds;
}
