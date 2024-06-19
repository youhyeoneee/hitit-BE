package com.pda.mydata_service.jpa;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, String> {
    Optional<List<BankAccount>> findByMydataUser_Id(int userId);
//    Optional<List<BankAccount>> findByUserIdAndBankName(int userId, String bankName);

    Optional<List<BankAccount>> findByMydataUser_IdAndBankName(int userId, String bankName);

}
