package com.pda.asset_service.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MydataInfoRepository extends JpaRepository<MydataInfo, Integer> {

    MydataInfo findByAccountNo(String accountNo);
}
