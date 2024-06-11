package com.pda.user_service.RDSTest;

import com.pda.user_service.jpa.RDSTest;
import com.pda.user_service.jpa.RDSTestRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class RDSConnectionTest {

    @Autowired
    private RDSTestRepository rdsTestRepository;

    @Test
    @Transactional
    public void testRDSConnection() {


        // 데이터 조회 테스트
        List<RDSTest> data = rdsTestRepository.findAll();
        for (RDSTest d : data) {
            System.out.println(d.toString());
        }
    }
}
