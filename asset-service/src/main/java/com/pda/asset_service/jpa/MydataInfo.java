package com.pda.asset_service.jpa;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "mydata_info")
public class MydataInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "asset_type")
    private String assetType;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "account_type")
    private String accountType;

    @Column(name = "account_no")
    private String accountNo;
}
