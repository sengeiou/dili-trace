package com.dili.trace.service;

import java.util.List;
import java.util.stream.Collectors;

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
    @Autowired
    RegisterBillService registerBillService;

    /**
     * 通过登记单来更新二维码状态
     */
    public void updateQrItemDetail(RegisterBill registerBill) {

        User userItem = this.userService.get(registerBill.getUserId());
        if (userItem == null) {
            throw new BusinessException("未能查询到用户信息");
        }
        // 查询并添加UserQrItem
        UserQrItem qrItem = new UserQrItem();
        qrItem.setUserId(userItem.getId());
        qrItem.setQrItemType(QrItemTypeEnum.UPSTREAM.getCode());
        List<UserQrItem> qrItemList = this.userQrItemService.listByExample(qrItem);
        if (qrItemList.isEmpty()) {
            qrItem.setQrItemStatus(QrItemStatusEnum.GREEN.getCode());
            this.userQrItemService.insertSelective(qrItem);
        } else {
            qrItem = qrItemList.stream().findFirst().orElse(null);
        }
        // 查询并添加UserQrItemDetail
        UserQrItemDetail qrItemDetail = new UserQrItemDetail();
        qrItemDetail.setObjectId(String.valueOf(registerBill.getId()));
        qrItemDetail.setUserQrItemId(qrItem.getId());
        if (this.listByExample(qrItemDetail).stream().count() == 0) {
            this.insertSelective(qrItemDetail);
        }
        // 查询所有UserQrItemDetail并进行状态判断
        UserQrItemDetail query = new UserQrItemDetail();
        query.setUserQrItemId(qrItem.getId());
        List<RegisterBill> billList = this.listByExample(query).stream()
                .map(detail -> Long.parseLong(detail.getObjectId())).map(billId -> this.registerBillService.get(billId))
                .collect(Collectors.toList());
        boolean withoutUrl = billList.stream().anyMatch(bill -> {
            return StringUtils.isAllBlank(registerBill.getOriginCertifiyUrl(), registerBill.getDetectReportUrl());
        });
        if (withoutUrl) {
            qrItem.setQrItemStatus(QrItemStatusEnum.YELLOW.getCode());
            this.userQrItemService.updateSelective(qrItem);
        }

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

        UserQrItem qrItem = new UserQrItem();
        qrItem.setUserId(userItem.getId());
        qrItem.setQrItemType(QrItemTypeEnum.UPSTREAM.getCode());
        List<UserQrItem> qrItemList = this.userQrItemService.listByExample(qrItem);
        if (qrItemList.isEmpty()) {
            qrItem.setQrItemStatus(QrItemStatusEnum.GREEN.getCode());
            this.userQrItemService.insertSelective(qrItem);
        } else {
            qrItem = qrItemList.stream().findFirst().orElse(null);
        }

        // 查询所有UserQrItemDetail并进行状态判断
        UserQrItemDetail query = new UserQrItemDetail();
        query.setUserQrItemId(qrItem.getId());
        List<UpStream> upStreamList = this.listByExample(query).stream()
                .map(detail -> Long.parseLong(detail.getObjectId()))
                .map(upStreamId -> this.upStreamService.get(upStreamId)).collect(Collectors.toList());
        boolean withoutAllInfo = upStreamList.stream().anyMatch(item -> {
            if (UpStreamTypeEnum.CORPORATE.getCode().equals(item.getUpstreamType())) {
                if (StringUtils.isAnyBlank(item.getName(), item.getBusinessLicenseUrl(), item.getLicense(),
                        item.getLicenseUrl(), item.getIdCard(), item.getLegalPerson(), item.getTelphone())) {
                    return true;
                }
            } else {
                if (StringUtils.isAnyBlank(item.getName(), item.getCardNoFrontUrl(), item.getCardNoBackUrl(),
                        item.getIdCard(), item.getTelphone())) {
                    return true;
                }
            }
            return false;
        });
        if (withoutAllInfo) {
            qrItem.setQrItemStatus(QrItemStatusEnum.YELLOW.getCode());
            this.userQrItemService.updateSelective(qrItem);
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
        UserQrItem qrItem = new UserQrItem();
        qrItem.setUserId(userItem.getId());
        qrItem.setQrItemType(QrItemTypeEnum.USER.getCode());
        List<UserQrItem> qrItemList = this.userQrItemService.listByExample(qrItem);
        if (qrItemList.isEmpty()) {
            qrItem.setQrItemStatus(QrItemStatusEnum.GREEN.getCode());
            this.userQrItemService.insertSelective(qrItem);
        } else {
            qrItem = qrItemList.stream().findFirst().orElse(null);
        }

        UserQrItemDetail qrItemDetail = new UserQrItemDetail();
        qrItemDetail.setObjectId(String.valueOf(userId));
        qrItemDetail.setUserQrItemId(qrItem.getId());
        if (this.listByExample(qrItemDetail).stream().count() == 0) {
            this.insertSelective(qrItemDetail);
        }

        // 查询所有UserQrItemDetail并进行状态判断
        UserQrItemDetail query = new UserQrItemDetail();
        query.setUserQrItemId(qrItem.getId());
        List<User> upStreamList = this.listByExample(query).stream().map(detail -> Long.parseLong(detail.getObjectId()))
                .map(upStreamId -> this.userService.get(upStreamId)).collect(Collectors.toList());
        boolean withoutAllInfo = upStreamList.stream().anyMatch(item -> {
            if (UserTypeEnum.CORPORATE.getCode().equals(item.getUserType())) {
                if (StringUtils.isAnyBlank(item.getName(), item.getBusinessLicenseUrl(), item.getLicense(),
                        item.getLicenseUrl(), item.getCardNo(), item.getLegalPerson(), item.getPhone())
                        || item.getMarketId() == null) {
                    return true;
                }
            } else {
                if (StringUtils.isAnyBlank(item.getName(), item.getCardNoFrontUrl(),
                        item.getCardNoBackUrl(), item.getCardNo(), item.getPhone())
                        || item.getMarketId() == null) {
                    return true;
                }
            }
            return false;
        });
        if (withoutAllInfo) {
            qrItem.setQrItemStatus(QrItemStatusEnum.YELLOW.getCode());
            this.userQrItemService.updateSelective(qrItem);
        }

    }

}