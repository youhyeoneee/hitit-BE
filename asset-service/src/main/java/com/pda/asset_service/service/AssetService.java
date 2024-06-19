package com.pda.asset_service.service;

import com.pda.asset_service.dto.MydataInfoDto;
import com.pda.asset_service.dto.UserAccountInfoDto;


import java.util.List;

public interface AssetService {
    List<MydataInfoDto> mydataLink(UserAccountInfoDto userAccountInfoDto);

}
