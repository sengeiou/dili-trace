package com.dili.trace.api.client;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.BasePage;
import com.dili.trace.AutoWiredBaseTest;
import com.dili.trace.api.input.ProductStockQueryDto;
import com.dili.trace.domain.ProductStock;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;

@TestInstance(Lifecycle.PER_CLASS)
public class ClientBatchStockApiTest extends AutoWiredBaseTest {
    @Autowired
    ClientProductStockApi clientBatchStockApi;

    @Test
    public void listSellersProductStock() {
        ProductStockQueryDto input = new ProductStockQueryDto();
        input.setUserId(2L);
        BaseOutput<BasePage<ProductStock>> out = this.clientBatchStockApi.listSellersProductStock(input);
        assertNotNull(out);
        assertTrue(out.isSuccess());
        List<ProductStock> list = out.getData().getDatas();
        assertNotNull(list);
        assertTrue(list.size()>0);
    }
}