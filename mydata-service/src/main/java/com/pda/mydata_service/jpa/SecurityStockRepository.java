package com.pda.mydata_service.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SecurityStockRepository extends JpaRepository<SecurityStock, SecurityStockId> {
}

