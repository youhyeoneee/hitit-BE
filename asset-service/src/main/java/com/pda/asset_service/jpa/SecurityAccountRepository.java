package com.pda.asset_service.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SecurityAccountRepository extends JpaRepository<SecurityAccount, String> {
}
