package com.pda.asset_service.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SecurityAccountRepository extends JpaRepository<SecurityAccount, String> {
    Optional<List<SecurityAccount>> findByUserId(int userId);

    Optional<SecurityAccount> findByUserIdAndSecurityNameAndAccountType(int userId, String securityName, String accountType    );
}
