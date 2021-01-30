package com.dili.trace.service;

import com.dili.commons.glossary.YesOrNoEnum;
import com.dili.customer.sdk.domain.dto.CustomerExtendDto;
import com.dili.trace.api.input.UserQueryDto;
import com.dili.trace.domain.UserInfo;
import one.util.streamex.StreamEx;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.Date;

/**
 * 用户同步service
 */
@Service
public class UserInfoService extends TraceBaseService<UserInfo, Long> {
    @Autowired
    SyncRpcService syncRpcService;

    /**
     * 保存或者查询需要同步的用户
     *
     * @param userId
     * @param marketId
     * @return
     */
    public UserInfo saveUserInfo(Long userId, Long marketId) {

        UserQueryDto query = new UserQueryDto();
        query.setUserId(userId);
        query.setMarketId(marketId);
        UserInfo userInfo = StreamEx.of(this.listByExample(query)).findFirst().orElseGet(() -> {

            UserInfo newDomain = new UserInfo();
            newDomain.setUserId(userId);
            newDomain.setMarketId(marketId);
            newDomain.setCreated(new Date());
            newDomain.setModified(new Date());
            newDomain.setLastSyncSuccess(YesOrNoEnum.NO.getCode());
            try {
                this.insertSelective(newDomain);
                return newDomain;
            }catch (DuplicateKeyException e){
                return StreamEx.of(this.listByExample(query)).findFirst().orElse(null);
            }

        });
        this.syncRpcService.syncRpcUser(userInfo);
        return userInfo;
    }

    /**
     * 更新user信息
     *
     * @param id
     * @param customerExtendDto
     * @return
     */
    public int updateUserInfoByCustomerExtendDto(Long id, CustomerExtendDto customerExtendDto) {
        LOGGER.debug("updateUserInfoByCustomerExtendDto id={}", id);
        if (id == null || customerExtendDto == null) {
            return 0;
        }
        try {
            UserInfo userInfo = new UserInfo();
            userInfo.setId(id);
            userInfo.setLastSyncSuccess(YesOrNoEnum.YES.getCode());
            userInfo.setLastSyncTime(new Date());
            userInfo.setCreated(Date.from(customerExtendDto.getCreateTime().atZone(ZoneId.systemDefault()).toInstant()));
            userInfo.setModified(Date.from(customerExtendDto.getModifyTime().atZone(ZoneId.systemDefault()).toInstant()));
            return this.updateSelective(userInfo);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }

        return 0;
    }


}
