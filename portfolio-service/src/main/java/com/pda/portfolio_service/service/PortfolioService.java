package com.pda.portfolio_service.service;

import com.pda.portfolio_service.jpa.PortfolioRepository;
import com.pda.user_service.dto.SignupUserDto;
import com.pda.user_service.jpa.User;
import com.pda.user_service.jpa.UserRepository;
import com.pda.utils.exception.DuplicatedEmailException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@PropertySource(value = {"env.properties"})
public class PortfolioService {
    @Autowired
    private PortfolioRepository portfolioRepository;

    public HititResponseDto getHititPortfolio() {
        return portfolioRepository.findAll();
    }
}