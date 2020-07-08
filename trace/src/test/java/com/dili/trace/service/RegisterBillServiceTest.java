package com.dili.trace.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import com.dili.trace.AutoWiredBaseTest;
import com.dili.trace.api.input.CheckInApiInput;
import com.dili.trace.domain.CheckinOutRecord;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.dto.OperatorUser;
import com.dili.trace.enums.BillVerifyStatusEnum;
import com.dili.trace.enums.CheckinOutTypeEnum;
import com.dili.trace.enums.CheckinStatusEnum;
import com.google.common.collect.Lists;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import one.util.streamex.StreamEx;

public class RegisterBillServiceTest extends AutoWiredBaseTest {
    @Autowired
    RegisterBillService billService;
    @Autowired
    CheckinOutRecordService checkinOutRecordService;
    @Autowired
    TradeDetailService tradeDetailService;

    @Test
    public void doVerifyBeforeCheckIn() {
        RegisterBill query = new RegisterBill();
        query.setVerifyStatus(BillVerifyStatusEnum.NONE.getCode());
        RegisterBill input = StreamEx.of(this.billService.listByExample(query)).findFirst().orElse(null);
        assertNotNull(input);
        OperatorUser operatorUser = new OperatorUser(1L, "test");
        input.setVerifyStatus(BillVerifyStatusEnum.PASSED.getCode());
        Long billId = this.billService.doVerifyBeforeCheckIn(input, operatorUser);
        assertNotNull(billId);
    }

    @Test
    public void doVerifyAfterCheckIn() {

        RegisterBill query = new RegisterBill();
        query.setVerifyStatus(BillVerifyStatusEnum.NONE.getCode());
        RegisterBill input = StreamEx.of(this.billService.listByExample(query)).findFirst().orElse(null);
        assertNotNull(input);
        assertTrue(BillVerifyStatusEnum.NONE.equalsToCode(input.getVerifyStatus()));
        CheckInApiInput checkInApiInput = new CheckInApiInput();
        checkInApiInput.setBillIdList(Lists.newArrayList(input.getId()));
        checkInApiInput.setCheckinStatus(CheckinStatusEnum.ALLOWED.getCode());
        List<CheckinOutRecord> checkInList = this.checkinOutRecordService.doCheckin(new OperatorUser(1L, "test"),
                checkInApiInput);
        assertTrue(checkInList.size() == 1);
        CheckinOutRecord recordItem = checkInList.get(0);
        assertTrue(CheckinOutTypeEnum.IN.equalsToCode(recordItem.getInout()));
        assertTrue(CheckinStatusEnum.ALLOWED.equalsToCode(recordItem.getStatus()));

        OperatorUser operatorUser = new OperatorUser(1L, "test");
        input.setVerifyStatus(BillVerifyStatusEnum.PASSED.getCode());
        Long billId = this.billService.doVerifyAfterCheckIn(input, operatorUser);
        assertNotNull(billId);

    }

    @Test
    @Transactional
    public void selectByIdForUpdate() {
        this.billService.selectByIdForUpdate(5L).ifPresent(bill -> {
            System.out.println(bill);
        });
    }

    
    @Test
    public void viewTradeDetailBill() {
        this.billService.viewTradeDetailBill(182L,5071L);
    }

}