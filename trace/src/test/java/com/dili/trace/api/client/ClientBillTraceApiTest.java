package com.dili.trace.api.client;

import com.dili.trace.AutoWiredBaseTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ClientBillTraceApiTest extends AutoWiredBaseTest {
    @Autowired
    ClientBillTraceApi clientBillTraceApi;

    @Test
    public void dd(){
        TradeRequestInputDto inputDto=new TradeRequestInputDto();

        this.clientBillTraceApi.viewBillTrace(null)
    }
}