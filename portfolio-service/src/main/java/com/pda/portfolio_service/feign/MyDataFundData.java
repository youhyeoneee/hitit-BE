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
    private int user_test_score;

    // userTestScore를 제외한 생성자
    public MyDataFundData(int user_id, List<MyDataTransaction> transactions, List<String> stockBalance, int age, int wealth) {
        this.user_id = user_id;
        this.transactions = transactions;
        this.stockBalance = stockBalance;
        this.age = age;
        this.wealth = wealth;
    }
}
