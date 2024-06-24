package com.pda.asset_service.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "bank_accounts")
//@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BankAccount {

    @Id
    @Column(name = "account_no")
    private String accountNo;

    @Column(name ="bank_name")
    private String bankName;

    @Column(name ="account_type")
    private String accountType;

    private String name;

    private int balance;

    @Column(name ="created_at")
    private Date createdAt;

    @Column(name = "user_id")
    private int userId;
}
