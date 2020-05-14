package com.dili.trace.service;

import java.util.List;

import com.dili.common.exception.BusinessException;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.domain.UpStream;
import com.dili.trace.domain.User;
import com.dili.trace.domain.UserQrItem;
import com.dili.trace.domain.UserQrItemDetail;
import com.dili.trace.glossary.QrItemStatusEnum;
import com.dili.trace.glossary.QrItemTypeEnum;
import com.dili.trace.glossary.UpStreamTypeEnum;
import com.dili.trace.glossary.UserTypeEnum;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserQrItemDetailService extends BaseServiceImpl<UserQrItemDetail, Long> {
    @Autowired
    UserService userService;
    @Autowired
    UserQrItemService userQrItemService;
    @Autowired
    UpStreamService upStreamService;

    /**
     * 通过登记单来更新二维码状态
     */
    public void updateQrItemDetail(RegisterBill registerBill) {
        UserQrItem queryCondition = new UserQrItem();
        queryCondition.setQrItemType(QrItemTypeEnum.BILL.getCode());

    }

    /**
     * 通过上游信息来更新二维码状态
     */
    public void updateQrItemDetail(UpStream upStream, Long userId) {
        User userItem = this.userService.get(userId);
        if (userItem == null) {
            throw new BusinessException("未能查询到用户信息");
        }
        UpStream upStreamItem = this.upStreamService.get(upStream.getId());
        if (upStreamItem == null) {
            throw new BusinessException("未能查询到上游信息");
        }
        QrItemStatusEnum itemStatus = QrItemStatusEnum.GREEN;
        if (UpStreamTypeEnum.CORPORATE.getCode().equals(upStreamItem.getUpstreamType())) {
            if (StringUtils.isAnyBlank(upStreamItem.getName(), upStreamItem.getLicense(), upStreamItem.getTelphone(),
            upStreamItem.get.getLicenseUrl(), userItem.getCardNo(), userItem.getLegalPerson(), userItem.getPhone())
            || userItem.getMarketId() == null) {
        itemStatus = QrItemStatusEnum.YELLOW;
    }
        } else {

        }

    }

    /**
     * 通过用户信息来更新二维码状态
     */
    public void updateQrItemDetail(User user) {
        Long userId = user.getId();
        User userItem = this.userService.get(userId);
        if (userItem == null) {
            throw new BusinessException("未能查询到用户信息");
        }
        QrItemStatusEnum itemStatus = QrItemStatusEnum.GREEN;
        if (UserTypeEnum.CORPORATE.getCode().equals(userItem.getUserType())) {
            if (StringUtils.isAnyBlank(userItem.getName(), userItem.getBusinessLicenseUrl(), userItem.getLicense(),
                    userItem.getLicenseUrl(), userItem.getCardNo(), userItem.getLegalPerson(), userItem.getPhone())
                    || userItem.getMarketId() == null) {
                itemStatus = QrItemStatusEnum.YELLOW;
            }
        } else {
            if (StringUtils.isAnyBlank(userItem.getName(), userItem.getCardNoFrontUrl(), userItem.getCardNoBackUrl(),
                    userItem.getCardNo(), userItem.getPhone()) || userItem.getMarketId() == null) {
                itemStatus = QrItemStatusEnum.YELLOW;
            }
        }
        UserQrItem qrItem = new UserQrItem();
        qrItem.setUserId(userId);
        qrItem.setQrItemType(QrItemTypeEnum.USER.getCode());
        List<UserQrItem> qrItemList = this.userQrItemService.listByExample(qrItem);
        if (qrItemList.isEmpty()) {
            this.userQrItemService.insertSelective(qrItem);
        } else {
            qrItem = qrItemList.stream().findFirst().orElse(null);
        }
        if (!itemStatus.getCode().equals(qrItem.getQrItemStatus())) {
            qrItem.setQrItemStatus(itemStatus.getCode());
            this.userQrItemService.updateSelective(qrItem);
        }
        UserQrItemDetail qrItemDetail = new UserQrItemDetail();
        qrItemDetail.setObjectId(String.valueOf(userId));
        qrItemDetail.setUserQrItemId(qrItem.getId());
        if (this.listByExample(qrItemDetail).stream().count() == 0) {
            this.insertSelective(qrItemDetail);
        }

    }

}