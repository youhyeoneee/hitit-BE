package com.pda.user_service.controller;

import com.pda.user_service.jpa.RDSTest;
import com.pda.user_service.service.RDSTestServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
public class RDSTestController {
    RDSTestServiceImpl rDSTestService;

    @GetMapping("/test")
    public List<RDSTest> findAllRDS(){
        return rDSTestService.findAllData();
    }

    @GetMapping("/testId")
    public Optional<RDSTest> findById(){
        return rDSTestService.findById();
    }



}

