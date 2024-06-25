

package com.pda.portfolio_service.jpa;

import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.util.Date;
import java.util.Objects;

import java.io.Serializable;

@Embeddable
@Getter
public class FundPricesId implements Serializable {
    private String fundCode;
    private Date date;

    // 기본 생성자
    public FundPricesId() {}

    // 매개변수 있는 생성자
    public FundPricesId(String fundCode, Date date) {
        this.fundCode = fundCode;
        this.date = date;
    }


    // equals()와 hashCode() 메서드 구현
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FundPricesId that = (FundPricesId) o;
        return fundCode.equals(that.fundCode) && date.equals(that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fundCode, date);
    }
}
