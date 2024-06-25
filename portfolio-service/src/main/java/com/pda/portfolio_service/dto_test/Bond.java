package com.pda.portfolio_service.dto_test;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SecondaryRow;

import java.util.Date;

@Getter
@Setter
public class Bond {
    private String bond_name;
    private Date expiredDate;
    private Float duration;
    private String credit;
    private Float weight;
}
