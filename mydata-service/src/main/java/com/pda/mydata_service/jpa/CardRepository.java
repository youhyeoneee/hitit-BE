package com.pda.mydata_service.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<Card, String> {
    Optional<List<Card>> findByMydataUser_Id(int userId);

    Optional<List<Card>> findByMydataUser_IdAndCompanyName(int userId, String cardName);

}
