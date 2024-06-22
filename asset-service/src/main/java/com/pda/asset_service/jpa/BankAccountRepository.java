package com.pda.asset_service.jpa;


import lombok.AllArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, String> {

    Optional<List<BankAccount>> findByUserId(int userId);
}