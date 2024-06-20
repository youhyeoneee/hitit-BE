package com.pda.mydata_service.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LoanRepository extends JpaRepository<Loan, String> {
    Optional<List<Loan>> findByMydataUser_Id(int userId);

    Optional<List<Loan>> findByMydataUser_IdAndCompanyName(int userId, String companyName);
}
