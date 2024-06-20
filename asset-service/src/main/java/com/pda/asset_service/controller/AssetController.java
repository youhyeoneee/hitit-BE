package com.pda.asset_service.controller;

import com.pda.asset_service.dto.MydataInfoDto;
import com.pda.asset_service.dto.UserAccountInfoDto;
import com.pda.asset_service.service.AssetServiceImpl;
import com.pda.utils.api_utils.ApiUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/assets")
@AllArgsConstructor
public class AssetController {

    private final AssetServiceImpl assetService;

    @PostMapping("/mydata-link")
    public ApiUtils.ApiResult<List<MydataInfoDto>> linkMydata(@RequestBody UserAccountInfoDto userAccountInfoDto){

        List<MydataInfoDto> bankAccountsLinkInfo = assetService.linkMydata(userAccountInfoDto);


        return ApiUtils.success(bankAccountsLinkInfo);
    }
}
