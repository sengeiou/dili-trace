package com.dili.trace.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import com.dili.trace.AutoWiredBaseTest;
import com.dili.trace.domain.ProductStock;
import com.dili.trace.domain.CheckinOutRecord;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.domain.TradeDetail;
import com.dili.trace.domain.User;
import com.dili.trace.dto.OperatorUser;
import com.dili.trace.enums.BillVerifyStatusEnum;
import com.dili.trace.enums.CheckinStatusEnum;

import com.dili.trace.enums.TradeOrderTypeEnum;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

public class TradeServiceTest extends AutoWiredBaseTest {
    @Autowired
    TradeService tradeService;
    @Autowired
    TradeDetailService tradeDetailService;
    @Autowired
    TradeRequestService tradeRequestService;
    @Autowired
    ProductStockService batchStockService;

    @Test
    public void createBatchStockAfterVerifiedAndCheckin_verifyBeforeCheckin() {
        RegisterBill bill = super.buildBill();
        bill.setUpStreamId(1L);
        RegisterBill billItem = super.createRegisterBill(bill);
        Long billId = super.doVerifyBeforeCheckIn(billItem.getBillId(), BillVerifyStatusEnum.PASSED);
        Pair<CheckinOutRecord, TradeDetail> p = super.doCheckIn(billId, CheckinStatusEnum.ALLOWED);
        TradeDetail tradeDetailItem = p.getValue();
        this.tradeService.createBatchStockAfterVerifiedAndCheckin(billId, tradeDetailItem.getId(), Optional.ofNullable(new OperatorUser(1L, "")));
    }

    @Test
    public void createBatchStockAfterVerifiedAndCheckin_verifyAfterCheckin() {
        RegisterBill bill = super.buildBill();
        bill.setUpStreamId(1L);
        RegisterBill billItem = super.createRegisterBill(bill);
        Pair<CheckinOutRecord, TradeDetail> p = super.doCheckIn(billItem.getId(), CheckinStatusEnum.ALLOWED);

        Long billId = super.doVerifyAfterCheckIn(billItem.getBillId(), BillVerifyStatusEnum.PASSED);

        TradeDetail tradeDetailItem = p.getValue();
        this.tradeService.createBatchStockAfterVerifiedAndCheckin(billId, tradeDetailItem.getId(), Optional.ofNullable(new OperatorUser(1L, "")));
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
        this.tradeService.createBatchStockAfterVerifiedAndCheckin(billId, tradeDetailItem.getId(), Optional.ofNullable(new OperatorUser(1L, "")));
        TradeDetail sellerTradeDetailItem = this.tradeDetailService.updateSellerTradeDetail(billItem, tradeDetailItem,
                tradeWeight);
        assertNotNull(sellerTradeDetailItem);
        ProductStock batchStock = batchStockService.get(sellerTradeDetailItem.getProductStockId());
        assertNotNull(batchStock);
        assertEquals(bill.getWeight().subtract(tradeWeight).compareTo(batchStock.getStockWeight()), 0);
    }

    @Test
    public void updateBuyerTradeDetail() {
        BigDecimal tradeWeight = BigDecimal.TEN;
        RegisterBill bill = super.buildBill();
        bill.setUpStreamId(1L);
        RegisterBill billItem = super.createRegisterBill(bill);
        Long billId = super.doVerifyBeforeCheckIn(billItem.getBillId(), BillVerifyStatusEnum.PASSED);
        Pair<CheckinOutRecord, TradeDetail> p = super.doCheckIn(billId, CheckinStatusEnum.ALLOWED);
        TradeDetail tradeDetailItem = p.getValue();
        this.tradeService.createBatchStockAfterVerifiedAndCheckin(billId, tradeDetailItem.getId(), Optional.ofNullable(new OperatorUser(1L, "")));
//
//        TradeDetail buyerTradeDetailItem = this.tradeDetailService.updateBuyerTradeDetail(billItem, tradeDetailItem,
//                tradeWeight, super.findUser(), 1L, TradeOrderTypeEnum.SELL);
//        assertNotNull(buyerTradeDetailItem);
//        ProductStock batchStock = batchStockService.get(buyerTradeDetailItem.getProductStockId());
//        assertNotNull(batchStock);
//        assertEquals(buyerTradeDetailItem.getStockWeight().compareTo(tradeWeight), 0);
    }

    @Transactional
    @Test
    public void updateSellerAndBuyerTradeDetail() {
        List<User> userList = super.findUsers();
        assertTrue(userList.size()>=2);
        User seller=userList.get(0);
        User buyer=userList.get(1);

        BigDecimal tradeWeight = BigDecimal.valueOf(30);
        RegisterBill bill = super.buildBill();
        bill.setUpStreamId(1L);
        bill.setUserId(seller.getId());
        bill.setName(seller.getName());

        RegisterBill billItem = super.createRegisterBill(bill);
        Long billId = super.doVerifyBeforeCheckIn(billItem.getBillId(), BillVerifyStatusEnum.PASSED);
        Pair<CheckinOutRecord, TradeDetail> p = super.doCheckIn(billId, CheckinStatusEnum.ALLOWED);
        TradeDetail tradeDetailItem = p.getValue();
        this.tradeService.createBatchStockAfterVerifiedAndCheckin(billId, tradeDetailItem.getId(), Optional.ofNullable(new OperatorUser(1L, "")));

        TradeDetail sellerTradeDetailItem = this.tradeDetailService.updateSellerTradeDetail(billItem, tradeDetailItem,
                tradeWeight);
        assertNotNull(sellerTradeDetailItem);
        ProductStock sellerBatchStock = batchStockService.get(sellerTradeDetailItem.getProductStockId());
        assertNotNull(sellerBatchStock);

//        TradeDetail buyerTradeDetailItem = this.tradeDetailService.updateBuyerTradeDetail(billItem, tradeDetailItem,
//                tradeWeight, buyer, 1L, TradeOrderTypeEnum.BUY);
//        assertNotNull(buyerTradeDetailItem);
//        ProductStock buyerBatchStock = batchStockService.get(buyerTradeDetailItem.getProductStockId());
//        assertNotNull(buyerBatchStock);
        // assertEquals(buyerTradeDetailItem.getStockWeight().compareTo(tradeWeight),
        // 0);
    }
}