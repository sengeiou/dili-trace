package com.dili.trace.service;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.dili.common.exception.BusinessException;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.dto.DTOUtils;
import com.dili.trace.api.dto.CheckInApiInput;
import com.dili.trace.domain.CheckinRecord;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.domain.User;
import com.dili.trace.glossary.CheckinStatusEnum;
import com.dili.trace.glossary.RegisterBillStateEnum;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CheckinRecordService extends BaseServiceImpl<CheckinRecord, Long> {
    @Autowired
    UserService userService;
    @Autowired
    RegisterBillService registerBillService;
    @Autowired
    SeparateSalesRecordService separateSalesRecordService;

    @Transactional
    public CheckinRecord doCheckin(User operateUser, CheckInApiInput checkInApiInput) {
        if (checkInApiInput == null || checkInApiInput.getBillIdList() == null
                || checkInApiInput.getCheckinStatus() == null) {
            throw new BusinessException("参数错误");
        }
        CheckinStatusEnum checkinStatusEnum = CheckinStatusEnum.fromCode(checkInApiInput.getCheckinStatus());

        if (checkinStatusEnum == null) {
            throw new BusinessException("参数错误");
        }
        List<RegisterBill> billList = checkInApiInput.getBillIdList().stream().map(billId -> {

            RegisterBill bill = this.registerBillService.get(billId);
            if (bill == null) {
                return null;
            } else {
                if (RegisterBillStateEnum.WAIT_AUDIT.getCode().equals(bill.getState())) {
                    return bill;
                }
            }

            return null;
        }).filter(Objects::nonNull).collect(Collectors.toList());
        if (billList.isEmpty()) {
            throw new BusinessException("没有可以进场的登记单");
        }

        // User user = this.userService.get(checkInApiInput.getUserId());
        // if (user == null) {
        // throw new BusinessException("数据错误");
        // }

        CheckinRecord checkinRecord = new CheckinRecord();
        checkinRecord.setCheckinStatus(checkinStatusEnum.getCode());
        checkinRecord.setOperatorId(operateUser.getId());
        checkinRecord.setOperatorName(operateUser.getName());
        checkinRecord.setRemark(checkInApiInput.getRemark());
        checkinRecord.setCreated(new Date());
        checkinRecord.setModified(new Date());
        int returnValue=this.insertSelective(checkinRecord);
        billList.stream().forEach(bill -> {
            this.separateSalesRecordService.checkInSeparateSalesRecord(checkinRecord.getId(), bill.getId());
            RegisterBill updatable = DTOUtils.newDTO(RegisterBill.class);
            updatable.setId(bill.getId());
            updatable.setState(RegisterBillStateEnum.WAIT_CHECK.getCode());
            this.registerBillService.updateSelective(updatable);
        });

        return checkinRecord;

    }

}