package com.dili.trace.service;

import com.dili.trace.AutoWiredBaseTest;
import com.dili.trace.domain.RegisterBill;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class RegisterBillHistoryServiceTest extends AutoWiredBaseTest {

    @Autowired
    RegisterBillHistoryService registerBillHistoryService;

    @Test
    public void createHistory() {
        RegisterBill bill=super.billService.listByExample(new RegisterBill()).stream().findAny().orElse(null);
        this.registerBillHistoryService.createHistory(bill);
    }

}