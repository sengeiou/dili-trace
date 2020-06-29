package com.dili.trace.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;

import com.dili.ss.dto.DTOUtils;
import com.dili.trace.AutoWiredBaseTest;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.domain.TradeDetail;
import com.dili.trace.domain.User;
import com.dili.trace.enums.CheckinStatusEnum;
import com.dili.trace.enums.CheckoutStatusEnum;
import com.dili.trace.enums.SaleStatusEnum;
import com.dili.trace.enums.TradeDetailStatusEnum;
import com.dili.trace.enums.TradeTypeEnum;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import one.util.streamex.StreamEx;

public class BatchStockServiceTest extends AutoWiredBaseTest {
    @Autowired
    BatchStockService batchStockService;
    @Autowired
    UserService userService;
    @Autowired
    RegisterBillService registerBillService;
    @Autowired
    TradeDetailService tradeDetailService;

    @Test
    public void createOrUpdateBatchStock() {
        User userQuery = DTOUtils.newDTO(User.class);
        userQuery.setPage(1);
        userQuery.setRows(1);
        User userItem = StreamEx.of(this.userService.listPageByExample(userQuery).getDatas()).findFirst().orElse(null);
        assertNotNull(userItem);
        RegisterBill billQuery = new RegisterBill();
        billQuery.setPage(1);
        billQuery.setRows(1);
        RegisterBill billItem = StreamEx.of(this.registerBillService.listPageByExample(billQuery).getDatas())
                .findFirst().orElse(null);
        assertNotNull(billItem);
        TradeDetail tradeDetail = new TradeDetail();
        tradeDetail.setBillId(billItem.getId());
        tradeDetail.setTradeType(TradeTypeEnum.NONE.getCode());
        tradeDetail.setStatus(TradeDetailStatusEnum.NONE.getCode());
        tradeDetail.setCheckinStatus(CheckinStatusEnum.NONE.getCode());
        tradeDetail.setCheckoutStatus(CheckoutStatusEnum.NONE.getCode());
        tradeDetail.setSaleStatus(SaleStatusEnum.FOR_SALE.getCode());
        tradeDetail.setBuyerId(userItem.getId());
        tradeDetail.setBuyerName(userItem.getName());
        tradeDetail.setStockWeight(BigDecimal.TEN);
        tradeDetail.setTotalWeight(BigDecimal.valueOf(20L));
        this.tradeDetailService.insertSelective(tradeDetail);

        this.batchStockService.createOrUpdateBatchStock(tradeDetail);
    }

}