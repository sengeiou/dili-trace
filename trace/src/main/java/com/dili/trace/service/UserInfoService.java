package com.dili.trace.service;

import com.dili.commons.glossary.YesOrNoEnum;
import com.dili.customer.sdk.domain.dto.CustomerExtendDto;
import com.dili.trace.api.input.UserQueryDto;
import com.dili.trace.domain.UserInfo;
import com.dili.trace.glossary.UserQrStatusEnum;
import one.util.streamex.StreamEx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.Date;

/**
 * 用户同步service
 */
@Service
public class UserInfoService extends TraceBaseService<UserInfo, Long> {

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
            newDomain.setPreQrStatus(UserQrStatusEnum.BLACK.getCode());
            newDomain.setQrStatus(UserQrStatusEnum.BLACK.getCode());
            newDomain.setLastSyncSuccess(YesOrNoEnum.NO.getCode());
            try {
                this.insertSelective(newDomain);
                return newDomain;
            } catch (DuplicateKeyException e) {
                return StreamEx.of(this.listByExample(query)).findFirst().orElse(null);
            }

        });
        return userInfo;
    }

    /**
     * 更新user信息
     *
     * @param id
     * @param extDto
     * @return
     */
    public int updateUserInfoByCustomerExtendDto(Long id, CustomerExtendDto extDto) {
        LOGGER.debug("updateUserInfoByCustomerExtendDto id={}", id);
        if (id == null || extDto == null) {
            return 0;
        }
        try {
            UserInfo userInfo = new UserInfo();
            userInfo.setId(id);
            userInfo.setMarketId(extDto.getCustomerMarket().getMarketId());
            userInfo.setName(extDto.getName());
            userInfo.setLastSyncSuccess(YesOrNoEnum.YES.getCode());
            userInfo.setLastSyncTime(new Date());
            userInfo.setCreated(Date.from(extDto.getCreateTime().atZone(ZoneId.systemDefault()).toInstant()));
            userInfo.setModified(Date.from(extDto.getModifyTime().atZone(ZoneId.systemDefault()).toInstant()));
            return this.updateSelective(userInfo);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }

        return 0;
    }


}
