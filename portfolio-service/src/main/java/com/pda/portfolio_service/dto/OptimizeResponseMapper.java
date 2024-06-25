package com.pda.portfolio_service.dto;

import com.pda.portfolio_service.dto.OptimizeResponseCamelCaseDto;
import com.pda.portfolio_service.feign.OptimizeResponseDto;

import java.util.stream.Collectors;

public class OptimizeResponseMapper {

    public static OptimizeResponseCamelCaseDto toCamelCase(OptimizeResponseDto snakeCaseDto) {
        if (snakeCaseDto == null) {
            return null;
        }

        OptimizeResponseCamelCaseDto.Response response = new OptimizeResponseCamelCaseDto.Response(
                snakeCaseDto.getResponse().getFunds().stream()
                        .map(fund -> new OptimizeResponseCamelCaseDto.Fund(
                                fund.getFundCode(),
                                fund.getStocks().stream()
                                        .map(stock -> new OptimizeResponseCamelCaseDto.Stock(
                                                stock.getStockCode(),
                                                stock.getStockName(),
                                                stock.getBadNewsTitle(),
                                                stock.getBadNewsUrl(),
                                                stock.getRev(),
                                                stock.getIncome()
                                        ))
                                        .collect(Collectors.toList())
                        ))
                        .collect(Collectors.toList()),
                snakeCaseDto.getResponse().getUserId(),
                snakeCaseDto.getResponse().getWeights()
        );

        return new OptimizeResponseCamelCaseDto(response);
    }
}
