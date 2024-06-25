package com.pda.mydata_service.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SecurityTransactionRepository extends JpaRepository<SecurityTransaction, String> {
    Optional<List<SecurityTransaction>> findBySecurityAccount_AccountNo(String accountNo);
}
