package com.pda.portfolio_service.jpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "users_portfolio")
public class UserPortfolio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer portfolio_id;

    public UserPortfolio(Integer id) {
        this.id = id;
    }

    public UserPortfolio(Integer id, Integer portfolio_id) {
        this.id = id;
        this.portfolio_id = portfolio_id;
    }
}
