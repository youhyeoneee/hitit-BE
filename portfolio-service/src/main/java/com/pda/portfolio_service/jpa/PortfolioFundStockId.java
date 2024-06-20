package com.pda.portfolio_service.jpa;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import java.util.Objects;

import java.io.Serializable;

@Embeddable
@Getter
public class PortfolioFundStockId implements Serializable {
    private String stockName;
    private String fundCode;

    // 기본 생성자
    public PortfolioFundStockId() {}

    // 매개변수 있는 생성자
    public PortfolioFundStockId(String stockName, String fundCode) {
        this.stockName = stockName;
        this.fundCode = fundCode;
    }


    // equals()와 hashCode() 메서드 구현
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PortfolioFundStockId that = (PortfolioFundStockId) o;
        return stockName.equals(that.stockName) && fundCode.equals(that.fundCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stockName, fundCode);
    }
}
