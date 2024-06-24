package com.pda.asset_service.jpa;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SecurityTransactionRepository extends JpaRepository<SecurityTransaction, String> {
}
