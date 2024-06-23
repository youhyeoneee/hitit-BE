package com.pda.asset_service.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {
    Optional<Account> findByAccountNo(String accountNo);

    Optional<Account> findByUserId(int userId);
}
