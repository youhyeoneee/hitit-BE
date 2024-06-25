package com.pda.asset_service.service;

import com.pda.asset_service.dto.SecurityAccountStocksDto;
import com.pda.asset_service.dto.SecurityStockResponseDto;

import java.util.List;
import java.util.Optional;

public interface SecurityStockService {
    Optional<List<SecurityAccountStocksDto>> getSecurityStocks(int userId);
}
