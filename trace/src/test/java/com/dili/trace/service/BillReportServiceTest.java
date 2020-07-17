package com.dili.trace.service;

import com.dili.trace.AutoWiredBaseTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class BillReportServiceTest extends AutoWiredBaseTest {
    @Autowired
    BillReportService billReportService;

    @Test
    public void queryBillReport() {
        this.billReportService.queryBillReport();
        ;
    }

}