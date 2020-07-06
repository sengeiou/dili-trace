package com.dili.trace.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;

import com.dili.trace.AutoWiredBaseTest;
import com.dili.trace.domain.BatchStock;
import com.dili.trace.domain.CheckinOutRecord;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.domain.TradeDetail;
import com.dili.trace.dto.OperatorUser;
import com.dili.trace.enums.BillVerifyStatusEnum;
import com.dili.trace.enums.CheckinStatusEnum;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TradeServiceTest extends AutoWiredBaseTest {
    @Autowired
    TradeService tradeService;
    @Autowired
    TradeDetailService tradeDetailService;
    @Autowired
    TradeRequestService tradeRequestService;
    @Autowired
    BatchStockService batchStockService;

    @Test
    public void createBatchStockAfterVerifiedAndCheckin_verifyBeforeCheckin() {
        RegisterBill bill = super.buildBill();
        bill.setUpStreamId(1L);
        RegisterBill billItem = super.createRegisterBill(bill);
        Long billId = super.doVerifyBeforeCheckIn(billItem.getBillId(), BillVerifyStatusEnum.PASSED);
        Pair<CheckinOutRecord, TradeDetail> p = super.doCheckIn(billId, CheckinStatusEnum.ALLOWED);
        TradeDetail tradeDetailItem = p.getValue();
        this.tradeService.createBatchStockAfterVerifiedAndCheckin(billId, tradeDetailItem, new OperatorUser(1L, ""));
    }

    @Test
    public void createBatchStockAfterVerifiedAndCheckin_verifyAfterCheckin() {
        RegisterBill bill = super.buildBill();
        bill.setUpStreamId(1L);
        RegisterBill billItem = super.createRegisterBill(bill);
        Pair<CheckinOutRecord, TradeDetail> p = super.doCheckIn(billItem.getId(), CheckinStatusEnum.ALLOWED);

        Long billId = super.doVerifyAfterCheckIn(billItem.getBillId(), BillVerifyStatusEnum.PASSED);

        TradeDetail tradeDetailItem = p.getValue();
        this.tradeService.createBatchStockAfterVerifiedAndCheckin(billId, tradeDetailItem, new OperatorUser(1L, ""));
    }

    @Test
    public void updateSellerTradeDetail() {
        BigDecimal tradeWeight = BigDecimal.TEN;

        RegisterBill bill = super.buildBill();
        bill.setUpStreamId(1L);

        RegisterBill billItem = super.createRegisterBill(bill);
        Long billId = super.doVerifyBeforeCheckIn(billItem.getBillId(), BillVerifyStatusEnum.PASSED);
        Pair<CheckinOutRecord, TradeDetail> p = super.doCheckIn(billId, CheckinStatusEnum.ALLOWED);
        TradeDetail tradeDetailItem = p.getValue();
        this.tradeService.createBatchStockAfterVerifiedAndCheckin(billId, tradeDetailItem, new OperatorUser(1L, ""));

        Long sellerTradeDetailId = this.tradeDetailService.updateSellerTradeDetail(billItem, tradeDetailItem,
                tradeWeight);
        TradeDetail sellerTradeDetailItem = this.tradeDetailService.get(sellerTradeDetailId);
        BatchStock batchStock = batchStockService.get(sellerTradeDetailItem.getBatchStockId());
        assertNotNull(sellerTradeDetailItem);
        assertNotNull(batchStock);
    }

    @Test
    public void updateBuyerTradeDetail() {
        RegisterBill bill = super.buildBill();
        bill.setUpStreamId(1L);
        RegisterBill billItem = super.createRegisterBill(bill);
        Long billId = super.doVerifyBeforeCheckIn(billItem.getBillId(), BillVerifyStatusEnum.PASSED);
        Pair<CheckinOutRecord, TradeDetail> p = super.doCheckIn(billId, CheckinStatusEnum.ALLOWED);
        TradeDetail tradeDetailItem = p.getValue();
        this.tradeService.createBatchStockAfterVerifiedAndCheckin(billId, tradeDetailItem, new OperatorUser(1L, ""));

        this.tradeDetailService.updateBuyerTradeDetail(billItem, tradeDetailItem, BigDecimal.TEN, super.findUser(), 1L);
    }

}