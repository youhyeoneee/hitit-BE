package com.pda.asset_service.service;

import com.pda.asset_service.jpa.MydataInfoRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class MydataServiceImpl implements MydataInfoService{

    private final MydataInfoRepository mydataInfoRepository;
}
