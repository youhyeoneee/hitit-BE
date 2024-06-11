package com.pda.user_service.service;

import com.pda.user_service.jpa.RDSTest;
import com.pda.user_service.jpa.RDSTestRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RDSTestServiceImpl implements RDSTestService {
    RDSTestRepository rdsTestRepository;

    public List<RDSTest> findAllData(){
        return rdsTestRepository.findAll();
    }

    public Optional<RDSTest> findById(){
        System.out.println("test");
        return rdsTestRepository.findById(1);
    }

}

