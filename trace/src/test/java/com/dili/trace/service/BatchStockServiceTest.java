package com.dili.trace.service;

import com.dili.trace.AutoWiredBaseTest;
import org.springframework.beans.factory.annotation.Autowired;

public class BatchStockServiceTest extends AutoWiredBaseTest {
    @Autowired
    ProductStockService batchStockService;
    @Autowired
    RegisterBillService registerBillService;
    @Autowired
    TradeDetailService tradeDetailService;


}