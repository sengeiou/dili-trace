package com.dili.trace.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import com.dili.common.exception.BusinessException;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.dto.IDTO;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.domain.UpStream;
import com.dili.trace.domain.User;
import com.dili.trace.domain.UserQrItem;
import com.dili.trace.domain.UserQrItemDetail;
import com.dili.trace.dto.RegisterBillDto;
import com.dili.trace.glossary.QrItemStatusEnum;
import com.dili.trace.glossary.QrItemTypeEnum;
import com.dili.trace.glossary.UpStreamTypeEnum;
import com.dili.trace.glossary.UserQrStatusEnum;
import com.dili.trace.glossary.UserTypeEnum;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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

    @PostConstruct
    public void init(){
        while(true){
            User userQuery=DTOUtils.newDTO(User.class);
            userQuery.mset(IDTO.AND_CONDITION_EXPR, "id not in(select user_id from user_qr_item)");
            userQuery.setPage(1);
            userQuery.setRows(50);
            List<User>userList=this.userService.listByExample(userQuery);
            if(userList.isEmpty()){
                break;
            }
            userList.stream().forEach(u->this.intUserQrItem(u.getId()));
        }




    }

    /**
     * 通过登记单来更新二维码状态
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateQrItemDetail(RegisterBill registerBill) {

        User userItem = this.userService.get(registerBill.getUserId());
        if (userItem == null) {
            throw new BusinessException("未能查询到用户信息");
        }
        Long userId = userItem.getId();
        // 查询并添加UserQrItem
        this.intUserQrItem(userId);
        UserQrItem qrItemCondition = new UserQrItem();
        qrItemCondition.setUserId(userItem.getId());
        qrItemCondition.setQrItemType(QrItemTypeEnum.BILL.getCode());
        UserQrItem qrItem = this.userQrItemService.listByExample(qrItemCondition).stream().findFirst().orElse(null);

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
        List<Long> idList = this.listByExample(query).stream().map(detail -> Long.parseLong(detail.getObjectId()))
                .collect(Collectors.toList());

        if (idList.isEmpty()) {
            return;
        }
        RegisterBillDto dto = DTOUtils.newDTO(RegisterBillDto.class);
        dto.setIdList(idList);

        List<RegisterBill> billList = this.registerBillService.listByExample(dto);

        // 删除有证明的登记单
        Long userQrItemId = qrItem.getId();
        billList.stream().filter(
                bill -> !StringUtils.isAllBlank(registerBill.getOriginCertifiyUrl(), registerBill.getDetectReportUrl()))
                .forEach(bill -> {
                    UserQrItemDetail deleteCondition = new UserQrItemDetail();
                    qrItemDetail.setObjectId(String.valueOf(bill.getId()));
                    qrItemDetail.setUserQrItemId(userQrItemId);
                    this.deleteByExample(deleteCondition);
                });
        // 查询剩余的登记单信息
        boolean withoutUrl = this.registerBillService.listByExample(dto).stream().anyMatch(bill -> {
            return StringUtils.isAllBlank(registerBill.getOriginCertifiyUrl(), registerBill.getDetectReportUrl());
        });
        if (withoutUrl) {
            qrItem.setQrItemStatus(QrItemStatusEnum.YELLOW.getCode());

        } else {
            qrItem.setQrItemStatus(QrItemStatusEnum.GREEN.getCode());
        }
        this.userQrItemService.updateSelective(qrItem);

        // TODO 红码逻辑
        // 30天内，累积检测不合格商品超50%以上，或检测不合格次数3次以上（待定）。*、
        // dto.setCreatedStart(
        // LocalDateTime.now().minusDays(30).format(DateTimeFormatter.ofPattern("yyyy-MM-dd
        // HH:mm:ss")));
        // List<RegisterBill> billList = this.registerBillService.listByExample(dto);
        this.updateUserQrStatus(userId);
    }

    /*
     *** 通过上游信息来更新二维码状态
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateQrItemDetail(UpStream upStream, Long userId) {
        User userItem = this.userService.get(userId);
        if (userItem == null) {
            throw new BusinessException("未能查询到用户信息");
        }
        UpStream upStreamItem = this.upStreamService.get(upStream.getId());
        if (upStreamItem == null) {
            throw new BusinessException("未能查询到上游信息");
        }

        UserQrItem qrItemCondition = new UserQrItem();
        qrItemCondition.setUserId(userItem.getId());
        qrItemCondition.setQrItemType(QrItemTypeEnum.UPSTREAM.getCode());
        UserQrItem qrItem = this.userQrItemService.listByExample(qrItemCondition).stream().findFirst().orElse(null);

        // 查询所有UserQrItemDetail并进行状态判断
        UserQrItemDetail query = new UserQrItemDetail();
        query.setUserQrItemId(qrItem.getId());
        List<UpStream> upStreamList = this.listByExample(query).stream()
                .map(detail -> Long.parseLong(detail.getObjectId()))
                .map(upStreamId -> this.upStreamService.get(upStreamId)).collect(Collectors.toList());
        boolean withoutAllNessaryInfo = upStreamList.stream().anyMatch(item -> {
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
        if (withoutAllNessaryInfo) {
            qrItem.setQrItemStatus(QrItemStatusEnum.YELLOW.getCode());
        } else {
            qrItem.setQrItemStatus(QrItemStatusEnum.GREEN.getCode());
        }
        this.userQrItemService.updateSelective(qrItem);
        this.updateUserQrStatus(userId);

    }

    /**
     * 通过用户信息来更新二维码状态
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateQrItemDetail(User user) {
        Long userId = user.getId();
        User userItem = this.userService.get(userId);
        if (userItem == null) {
            throw new BusinessException("未能查询到用户信息");
        }
        this.intUserQrItem(userId);
        UserQrItem qrItemCondition = new UserQrItem();
        qrItemCondition.setUserId(userItem.getId());
        qrItemCondition.setQrItemType(QrItemTypeEnum.USER.getCode());
        UserQrItem qrItem = this.userQrItemService.listByExample(qrItemCondition).stream().findFirst().orElse(null);

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
        boolean withoutAllNessaryInfo = upStreamList.stream().anyMatch(item -> {
            if (UserTypeEnum.CORPORATE.getCode().equals(item.getUserType())) {
                if (StringUtils.isAnyBlank(item.getName(), item.getBusinessLicenseUrl(), item.getLicense(),
                        item.getLicenseUrl(), item.getCardNo(), item.getLegalPerson(), item.getPhone())
                        || item.getMarketId() == null) {
                    return true;
                }
            } else {
                if (StringUtils.isAnyBlank(item.getName(), item.getCardNoFrontUrl(), item.getCardNoBackUrl(),
                        item.getCardNo(), item.getPhone()) || item.getMarketId() == null) {
                    return true;
                }
            }
            return false;
        });
        if (withoutAllNessaryInfo) {
            qrItem.setQrItemStatus(QrItemStatusEnum.YELLOW.getCode());
        } else {
            qrItem.setQrItemStatus(QrItemStatusEnum.GREEN.getCode());
        }
        this.userQrItemService.updateSelective(qrItem);
        this.updateUserQrStatus(userId);

    }

    /**
     * 通过当前条目判断并更新用户二维码状态
     */
    private int updateUserQrStatus(Long userId) {
        UserQrItem userQrItem = new UserQrItem();
        userQrItem.setUserId(userId);
        List<UserQrItem> qrItemList = this.userQrItemService.listByExample(userQrItem);
        Map<QrItemTypeEnum, List<UserQrItem>> typeItemMap = qrItemList.stream()
                .collect(Collectors.groupingBy(item -> QrItemTypeEnum.fromCode(item.getQrItemType())));

        User user = DTOUtils.newDTO(User.class);
        user.setId(userId);

        if (QrItemStatusEnum
                .fromCode(typeItemMap.get(QrItemTypeEnum.UPSTREAM).get(0).getQrItemStatus()) == QrItemStatusEnum.RED
                && QrItemStatusEnum.fromCode(
                        typeItemMap.get(QrItemTypeEnum.BILL).get(0).getQrItemStatus()) == QrItemStatusEnum.RED) {
            user.setQrStatus(UserQrStatusEnum.RED.getCode());
        } else if (QrItemStatusEnum
                .fromCode(typeItemMap.get(QrItemTypeEnum.USER).get(0).getQrItemStatus()) == QrItemStatusEnum.GREEN
                || QrItemStatusEnum.fromCode(
                        typeItemMap.get(QrItemTypeEnum.BILL).get(0).getQrItemStatus()) == QrItemStatusEnum.GREEN
                || QrItemStatusEnum.fromCode(
                        typeItemMap.get(QrItemTypeEnum.UPSTREAM).get(0).getQrItemStatus()) == QrItemStatusEnum.GREEN) {
            user.setQrStatus(UserQrStatusEnum.GREEN.getCode());
        } else {
            user.setQrStatus(UserQrStatusEnum.GREEN.getCode());
        }

        return this.userService.updateSelective(user);
    }

    /**
     * 初始化用户二维码
     */
    private boolean intUserQrItem(Long userId) {

        Stream.of(QrItemTypeEnum.BILL, QrItemTypeEnum.UPSTREAM, QrItemTypeEnum.USER).forEach(itemType -> {
            UserQrItem qrItem = new UserQrItem();
            qrItem.setUserId(userId);
            qrItem.setQrItemType(itemType.getCode());
            // 默认为红色(RED)状态
            if (this.userQrItemService.listByExample(qrItem).isEmpty()) {
                qrItem.setQrItemStatus(QrItemStatusEnum.RED.getCode());
                this.userQrItemService.insertSelective(qrItem);
            }
        });
        return true;
    }

}