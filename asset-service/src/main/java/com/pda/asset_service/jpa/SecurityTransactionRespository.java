package com.pda.asset_service.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SecurityTransactionRespository extends JpaRepository<BankAccount, String> {
}
