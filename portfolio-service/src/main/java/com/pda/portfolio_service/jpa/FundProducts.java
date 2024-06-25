package com.pda.portfolio_service.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.util.Date;


@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "fund_products_4")
public class FundProducts {
    @Id
    @Column(name = "fund_code")
    private String fundCode;

    @Column(name = "fund_name")
    private String fundName;

    @Column(name = "hashtag")
    private String hashtag;

    @Column(name = "std_price")
    private Float stdPrice;

    @Column(name = "set_date")
    private Date setDate;

    @Column(name = "fund_type")
    private String fundType;

    @Column(name = "fund_type_detail")
    private String fundTypeDetail;

    @Column(name = "set_amount")
    private Integer setAmount;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "risk_grade")
    private Integer riskGrade;

    @Column(name = "risk_grade_txt")
    private String riskGradeTxt;

    @Column(name = "drv_nav")
    private Float drvNav;

    @Column(name = "bond")
    private Float bond;

    @Column(name = "bond_foreign")
    private Float bondForeign;

    @Column(name = "stock")
    private Float stock;

    @Column(name = "stock_foreign")
    private Float stockForeign;

    @Column(name = "investment")
    private Float investment;

    @Column(name = "etc")
    private Float etc;

    @Column(name = "return_1m")
    private Float return1m;

    @Column(name = "return_3m")
    private Float return3m;

    @Column(name = "return_6m")
    private Float return6m;

    @Column(name = "return_1y")
    private Float return1y;

    @Column(name = "return_3y")
    private Float return3y;

    @Column(name = "return_5y")
    private Float return5y;

    @Column(name = "return_idx")
    private Float returnIdx;

    @Column(name = "return_ytd")
    private Float returnYtd;

    @Column(name = "arima_price")
    private Double arimaPrice;

    @Column(name = "arima_update")
    private Date arimaUpdate;

    @Column(name = "arima_percent")
    private Double arimaPercent;

    @Column(name = "stock_ratio")
    private Float stockRatio;

    @Column(name = "bond_ratio")
    private Float bondRatio;
}