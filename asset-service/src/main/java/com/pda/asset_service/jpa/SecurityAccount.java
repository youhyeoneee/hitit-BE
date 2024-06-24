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
@Table(name = "security_accounts")
public class SecurityAccount {

    @Id
    @Column(name = "account_no")
    private String accountNo;

    @Column(name = "security_name")
    private String securityName;

    @Column(name = "account_type")
    private String accountType;

    private Integer balance;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "user_id")
    private int userId;

}
