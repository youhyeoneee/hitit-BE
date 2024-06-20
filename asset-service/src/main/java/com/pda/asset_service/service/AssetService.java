package com.pda.asset_service.service;

import com.pda.asset_service.dto.MydataInfoDto;
import com.pda.asset_service.dto.UserAccountInfoDto;
import com.pda.user_service.jpa.User;


import java.util.List;

public interface AssetService {
    List<MydataInfoDto> linkMydata(int userId, UserAccountInfoDto userAccountInfoDto);

    // 나중에 kafka로 변경 예정
    User updateMydataStatus(int userId);

}
