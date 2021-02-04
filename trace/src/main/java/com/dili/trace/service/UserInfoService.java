package com.dili.trace.service;

import com.dili.commons.glossary.YesOrNoEnum;
import com.dili.customer.sdk.domain.dto.CustomerExtendDto;
import com.dili.trace.api.input.UserQueryDto;
import com.dili.trace.dao.UserMapper;
import com.dili.trace.domain.UserInfo;
import com.dili.trace.glossary.UserQrStatusEnum;
import one.util.streamex.StreamEx;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

/**
 * 用户同步service
 */
@Service
public class UserInfoService extends TraceBaseService<UserInfo, Long> {
    @Autowired
    UserMapper userMapper;

    /**
     * 根据userid查询 UserInfo
     *
     * @param userId
     * @return
     */
    public Optional<UserInfo> findByUserId(Long userId) {
        if (userId == null) {
            return Optional.empty();
        }
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(userId);
        return StreamEx.of(this.listByExample(userInfo)).findFirst();
    }

    public Optional<UserInfo> selectByUserIdForUpdate(Long userId) {

        return Optional.empty();
    }

    /**
     * 根据userid更新颜色
     *
     * @param userId
     * @param userQrStatusEnum
     * @return
     */
    public int updateUserQrByUserId(Long userId, UserQrStatusEnum userQrStatusEnum) {
        if (userId == null || userQrStatusEnum == null) {
            return 0;
        }
        UserInfo condition = new UserInfo();
        condition.setUserId(userId);


        UserInfo domain = new UserInfo();
        domain.setQrStatus(userQrStatusEnum.getCode());

        this.updateByExample(domain, condition);
        return 0;
    }

    /**
     * 保存或者查询需要同步的用户
     *
     * @param userId
     * @param marketId
     * @return
     */
    public Optional<UserInfo> saveUserInfo(Long userId, Long marketId) {
        try {
            UserInfo userInfo = new UserInfo();
            userInfo.setUserId(userId);
            userInfo.setMarketId(marketId);
            userInfo.setPreQrStatus(UserQrStatusEnum.BLACK.getCode());
            userInfo.setQrStatus(UserQrStatusEnum.BLACK.getCode());
            userInfo.setLastSyncSuccess(YesOrNoEnum.NO.getCode());
            this.userMapper.insertIgnoreUserInfo(userInfo);


            UserQueryDto query = new UserQueryDto();
            query.setUserId(userId);
            query.setMarketId(marketId);
            return StreamEx.of(this.listByExample(query)).findFirst();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return Optional.empty();

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
        UserInfo userInfoItem = this.get(id);
        if (userInfoItem == null) {
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
            userInfo.setState(extDto.getState());
            return this.updateSelective(userInfo);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }

        return 0;
    }

    /**
     * 根据CustomerExtendDto更新
     *
     * @param extDto
     * @return
     */
    public int updateUserInfoByCustomerExtendDto(CustomerExtendDto extDto) {
        return this.saveUserInfo(extDto.getId(), extDto.getCustomerMarket().getMarketId()).map(item -> {
            return this.updateUserInfoByCustomerExtendDto(item.getId(), extDto);
        }).orElse(0);
    }


}
