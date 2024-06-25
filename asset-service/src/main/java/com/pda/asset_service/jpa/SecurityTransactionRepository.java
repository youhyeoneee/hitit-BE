package com.pda.asset_service.jpa;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SecurityTransactionRepository extends JpaRepository<SecurityTransaction, String> {
    Optional<List<SecurityTransaction>> findAllByAccountNo(String accountNo);
}
