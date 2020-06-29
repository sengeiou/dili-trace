package com.dili.trace.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.dili.trace.AutoWiredBaseTest;
import com.dili.trace.domain.CheckinOutRecord;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.domain.TradeDetail;
import com.dili.trace.dto.OperatorUser;
import com.dili.trace.enums.BillVerifyStatusEnum;
import com.dili.trace.enums.CheckinOutTypeEnum;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

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
    public void doVerifyAfterCheckIn(){
        CheckinOutRecord recordQuery=new CheckinOutRecord();
        recordQuery.setInout(CheckinOutTypeEnum.IN.getCode());
       
        CheckinOutRecord recordItem= StreamEx.of( this.checkinOutRecordService.listByExample(recordQuery)).findFirst().orElse(null);
        assertNotNull(recordItem);
        Long tradeDetailId=recordItem.getTradeDetailId();
        TradeDetail tradeDetailItem=this.tradeDetailService.get(tradeDetailId);
        assertNotNull(tradeDetailItem);

        RegisterBill input = this.billService.get(tradeDetailItem.getBillId());
        assertNotNull(input);
        OperatorUser operatorUser = new OperatorUser(1L, "test");
        input.setVerifyStatus(BillVerifyStatusEnum.PASSED.getCode());
        Long billId = this.billService.doVerifyAfterCheckIn(input, operatorUser);
        assertNotNull(billId);


    }

}