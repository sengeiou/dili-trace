package com.dili.trace.service;

import com.dili.trace.async.AsyncService;
import com.dili.trace.domain.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 同步用户信息服务
 */
@Service
public class SyncUserInfoService {
    @Autowired
    UserInfoService userInfoService;
    @Autowired
    AsyncService asyncService;

    /**
     * 保存并同步用户信息
     *
     * @param userId
     * @param marketId
     * @return
     */
    public UserInfo saveAndSyncUserInfo(Long userId, Long marketId) {
        UserInfo userInfo = this.userInfoService.saveUserInfo(userId, marketId);
        this.asyncService.syncUserInfo(userInfo, customerExtendDto -> {
            this.userInfoService.updateUserInfoByCustomerExtendDto(userInfo.getId(), customerExtendDto);
        });
        return userInfo;
    }
}
