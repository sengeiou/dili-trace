package com.dili.trace.rpc.service;

import com.dili.customer.sdk.domain.Customer;
import com.dili.customer.sdk.domain.dto.CustomerExtendDto;
import com.dili.customer.sdk.domain.query.CustomerQueryInput;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.PageOutput;
import com.dili.trace.AutoWiredBaseTest;
import com.dili.trace.BaseTestWithouMVC;
import com.dili.trace.rpc.dto.CustomerQueryDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@EnableDiscoveryClient
public class CustomerRpcServiceTest extends BaseTestWithouMVC {
    @Autowired
    CustomerRpcService customerRpcService;

    @Test
    public void listSeller() {

        CustomerQueryDto query = new CustomerQueryDto();
        query.setPage(1);
        query.setRows(2);
        query.setKeyword("啊");
        Long marketId = 8L;
        query.setMarketId(marketId);

        PageOutput<List<CustomerExtendDto>> out = this.customerRpcService.listSeller(query);
        Assertions.assertNotNull(out);
    }

    @Test
    public void listBase() {
        CustomerQueryDto query = new CustomerQueryDto();
        query.setPage(1);
        query.setRows(20);
        query.setKeyword("啊");
        Long marketId = 8L;

        query.setMarketId(marketId);

        System.out.println(this.customerRpcService == null);
        PageOutput<List<CustomerExtendDto>> pg = this.customerRpcService.queryCustomerByCustomerQueryDto(query);
        Assertions.assertNotNull(pg);
    }
}
