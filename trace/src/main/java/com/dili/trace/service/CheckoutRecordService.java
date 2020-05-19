package com.dili.trace.service;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.dili.common.exception.BusinessException;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.dto.DTOUtils;
import com.dili.trace.api.dto.CheckOutApiInput;
import com.dili.trace.domain.CheckoutRecord;
import com.dili.trace.domain.SeparateSalesRecord;
import com.dili.trace.domain.User;
import com.dili.trace.glossary.CheckoutStatusEnum;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CheckoutRecordService extends BaseServiceImpl<CheckoutRecord, Long> {
    @Autowired
    UserService userService;
    @Autowired
    RegisterBillService registerBillService;
    @Autowired
    SeparateSalesRecordService separateSalesRecordService;

    @Transactional
    public CheckoutRecord doCheckout(User operateUser, CheckOutApiInput checkOutApiInput) {
        if (checkOutApiInput == null || checkOutApiInput.getSeparateSalesIdList() == null
                || checkOutApiInput.getCheckoutStatus() == null) {
            throw new BusinessException("参数错误");
        }
        CheckoutStatusEnum checkoutStatusEnum = CheckoutStatusEnum.fromCode(checkOutApiInput.getCheckoutStatus());

        if (checkoutStatusEnum == null) {
            throw new BusinessException("参数错误");
        }
        List<SeparateSalesRecord> recordList = checkOutApiInput.getSeparateSalesIdList().stream().map(billId -> {

            SeparateSalesRecord record = this.separateSalesRecordService.get(billId);
            if (record == null) {
                return null;
            } else {
                if (record.getCheckoutRecordId()==null) {
                    return record;
                }
            }

            return null;
        }).filter(Objects::nonNull).collect(Collectors.toList());
        if (recordList.isEmpty()) {
            throw new BusinessException("没有可以出场的交易单");
        }

        // User user = this.userService.get(checkInApiInput.getUserId());
        // if (user == null) {
        // throw new BusinessException("数据错误");
        // }

        CheckoutRecord checkoutRecord = new CheckoutRecord();
        checkoutRecord.setCheckoutStatus(checkoutStatusEnum.getCode());
        checkoutRecord.setOperatorId(operateUser.getId());
        checkoutRecord.setOperatorName(operateUser.getName());
        checkoutRecord.setRemark(checkOutApiInput.getRemark());
        checkoutRecord.setCreated(new Date());
        checkoutRecord.setModified(new Date());
        int returnValue=this.insertSelective(checkoutRecord);
        recordList.stream().forEach(record -> {
            SeparateSalesRecord updatable = DTOUtils.newDTO(SeparateSalesRecord.class);
            updatable.setId(record.getId());
            updatable.setCheckoutRecordId(checkoutRecord.getId());
            this.separateSalesRecordService.updateSelective(updatable);
        });

        return checkoutRecord;

    }

}