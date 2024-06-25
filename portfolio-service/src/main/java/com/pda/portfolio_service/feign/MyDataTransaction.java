package com.pda.portfolio_service.feign;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MyDataTransaction {
    private String date;
    private String type;
    private String code;
}
