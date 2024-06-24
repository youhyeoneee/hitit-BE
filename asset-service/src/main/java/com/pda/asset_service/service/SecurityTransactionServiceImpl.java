package com.pda.asset_service.service;


import com.pda.asset_service.jpa.SecurityTransactionRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class SecurityTransactionServiceImpl implements SecurityTransactionService{

    private final SecurityTransactionRepository securityTransactionRespository;
}
