package com.pda.investment_test.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/api/investment_tests")
public class InvestmentTestController {
    @GetMapping("/")
    public String test() {
        return "test";
    }
}
