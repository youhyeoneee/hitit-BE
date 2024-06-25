package com.pda.portfolio_service.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.pda.portfolio_service.jpa.UserPortfolio;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserPortfolioFundRequestDto {
    private Integer userId;

    public UserPortfolio convertToEntity() {
        return new UserPortfolio(userId);
    }
}