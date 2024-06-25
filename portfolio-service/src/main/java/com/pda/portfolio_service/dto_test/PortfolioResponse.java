package com.pda.portfolio_service.dto_test;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PortfolioResponse {
    private List<Portfolio> portfolios;
}

