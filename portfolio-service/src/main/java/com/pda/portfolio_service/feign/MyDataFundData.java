package com.pda.portfolio_service.feign;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MyDataFundData {
    private int user_id;
    private List<MyDataTransaction> transactions;
    private List<String> stockBalance;
    private int age;
    private int wealth;

}
