package com.pda.portfolio_service.jpa;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import java.util.Objects;

import java.io.Serializable;

@Embeddable
@Getter
public class PortfolioFundBondId implements Serializable {
    private String bondName;
    private String fundCode;

    // 기본 생성자
    public PortfolioFundBondId() {}

    // 매개변수 있는 생성자
    public PortfolioFundBondId(String bondName, String fundCode) {
        this.bondName = bondName;
        this.fundCode = fundCode;
    }


    // equals()와 hashCode() 메서드 구현
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PortfolioFundBondId that = (PortfolioFundBondId) o;
        return bondName.equals(that.bondName) && fundCode.equals(that.fundCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bondName, fundCode);
    }
}
