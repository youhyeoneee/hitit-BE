package com.pda.asset_service.jpa;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "cards")
public class Card {
    @Id
    @Column(name = "card_no")
    private String cardNo;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "card_name")
    private String cardName;

    @Column(name = "card_type")
    private String cardType;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "expired_at")
    private Date expiredAt;

    @Column(name = "account_no")
    private String accountNo;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private AssetUser assetUser;
}
