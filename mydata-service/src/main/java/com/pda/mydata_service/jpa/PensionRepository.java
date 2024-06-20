package com.pda.mydata_service.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PensionRepository extends JpaRepository<Pension, String> {

    Optional<List<Pension>> findByMydataUser_Id(int userId);

    Optional<List<Pension>> findByMydataUser_IdAndCompanyName(int userId, String companyName);
}
