package com.dili.trace.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.dili.trace.AutoWiredBaseTest;
import com.dili.trace.api.output.TraceDetailOutputDto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class BillTraceServiceTest extends AutoWiredBaseTest {
    @Autowired
    BillTraceService billTraceService;

    @Test
    public void viewBillTrace() {

        TraceDetailOutputDto out = this.billTraceService.viewBillTrace(10025L, 29L);
        assertNotNull(out);
    }

}