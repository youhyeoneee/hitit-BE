package com.pda.portfolio_service.jpa;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import java.util.Objects;

import java.io.Serializable;

@Embeddable
@Getter
public class HititPortfoliosFundProductId implements Serializable {
    private String fundCode;
    private Integer portfolioId;

    // 기본 생성자
    public HititPortfoliosFundProductId() {}

    // 매개변수 있는 생성자
    public HititPortfoliosFundProductId(String fundCode, Integer portfolioId) {
        this.fundCode = fundCode;
        this.portfolioId = portfolioId;
    }


    // equals()와 hashCode() 메서드 구현
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HititPortfoliosFundProductId that = (HititPortfoliosFundProductId) o;
        return fundCode.equals(that.fundCode) && portfolioId.equals(that.portfolioId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fundCode, portfolioId);
    }
}
