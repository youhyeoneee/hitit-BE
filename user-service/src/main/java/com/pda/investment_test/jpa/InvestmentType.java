package com.pda.investment_test.jpa;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum InvestmentType {
    CONSERVATIVE("안정형"),
    MODERATELY_CONSERVATIVE("안정추구형"),
    NEUTRAL("위험중립형"),
    AGGRESSIVE("적극투자형"),
    VERY_AGGRESSIVE("공격투자형");

    private final String description;
}
