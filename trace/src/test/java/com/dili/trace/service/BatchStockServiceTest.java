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
import com.dili.trace.enums.TradeTypeEnum;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import one.util.streamex.StreamEx;

public class BatchStockServiceTest extends AutoWiredBaseTest {
    @Autowired
    ProductStockService batchStockService;
    @Autowired
    UserService userService;
    @Autowired
    RegisterBillService registerBillService;
    @Autowired
    TradeDetailService tradeDetailService;


}