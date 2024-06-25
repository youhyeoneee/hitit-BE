package com.pda.portfolio_service.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OptimizeDto {
    private Integer userId;
    private Integer stockFundCount;
    private List<Integer> overseasIndexes;
    private List<FundProductDto> funds;
}
