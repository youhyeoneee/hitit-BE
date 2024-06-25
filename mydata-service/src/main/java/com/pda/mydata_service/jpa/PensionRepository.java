package com.pda.mydata_service.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PensionRepository extends JpaRepository<Pension, String> {

    Optional<List<Pension>> findByUserId(int userId);

    Optional<List<Pension>> findByUserIdAndCompanyName(int userId, String companyName);
    @Query("select DISTINCT p from Pension p where p.userId = :userId and p.retirementPensionClaimed = :retirementPensionClaimed")
    Optional<List<Pension>> findByUserIdAndRetirementPensionClaimed(Integer userId, String retirementPensionClaimed);
}
