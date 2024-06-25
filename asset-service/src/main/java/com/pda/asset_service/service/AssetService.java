package com.pda.asset_service.service;




import com.pda.asset_service.dto.MydataInfoDto;
import com.pda.asset_service.dto.PensionDto;
import com.pda.asset_service.dto.RetirementAccountDto;
import com.pda.asset_service.dto.UserAccountInfoDto;

import java.util.List;

public interface AssetService {
    List<MydataInfoDto> linkMydata(int userId, UserAccountInfoDto userAccountInfoDto);

    Integer getTotalAssets(int userId);

    List<RetirementAccountDto> getUnclaimedRetirementAccounts(int userId);
}
