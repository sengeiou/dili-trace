package com.dili.trace.api.client;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.BasePage;
import com.dili.trace.AutoWiredBaseTest;
import com.dili.trace.api.input.BatchStockQueryDto;
import com.dili.trace.domain.BatchStock;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;

@TestInstance(Lifecycle.PER_CLASS)
public class ClientBatchStockApiTest extends AutoWiredBaseTest {
    @Autowired
    ClientBatchStockApi clientBatchStockApi;

    @Test
    public void listSellersBatchStock() {
        BatchStockQueryDto input = new BatchStockQueryDto();
        input.setUserId(2L);
        BaseOutput<BasePage<BatchStock>> out = this.clientBatchStockApi.listSellersBatchStock(input);
        assertNotNull(out);
        assertTrue(out.isSuccess());
        List<BatchStock> list = out.getData().getDatas();
        assertNotNull(list);
        assertTrue(list.size()>0);
    }
}