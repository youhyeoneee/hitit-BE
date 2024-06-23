package com.pda.asset_service.jpa;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "user_id")
    private int userId;

    private String name;

    private String ssn;

    @Column(name = "company_name", columnDefinition = "VARCHAR(50) DEFAULT '신한투자증권'")
    private String companyName;

    @Column(name = "pension_type", columnDefinition = "VARCHAR(10) DEFAULT 'DC'")
    private String pensionType;

    @Column(name = "account_no", unique = true)
    private String accountNo;

    private String password;

    @Column(columnDefinition = "DECIMAL(18,2) DEFAULT 0")
    private int balance;

    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Date createdAt;

    @Column(name = "updated_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private Date updatedAt;
}
