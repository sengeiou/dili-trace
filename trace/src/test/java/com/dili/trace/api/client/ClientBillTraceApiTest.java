package com.dili.trace.api.client;

import com.dili.trace.AutoWiredBaseTest;
import com.dili.trace.api.input.TradeRequestInputDto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ClientBillTraceApiTest extends AutoWiredBaseTest {
    @Autowired
    ClientBillTraceApi clientBillTraceApi;

    @Test
    public void viewBillTrace() {
        TradeRequestInputDto inputDto = new TradeRequestInputDto();
        inputDto.setTradeRequestId(1L);
        this.clientBillTraceApi.viewBillTrace(inputDto);
    }
}