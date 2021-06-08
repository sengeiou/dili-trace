package com.dili.trace.rpc.service;

import com.dili.ss.dto.DTOUtils;
import com.dili.ss.util.AopTargetUtils;
import com.dili.trace.AutoWiredBaseTest;
import com.dili.trace.service.ExtCustomerService;
import com.dili.uap.sdk.domain.Firm;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.test.util.ReflectionTestUtils;

@EnableDiscoveryClient
public class ExtCustomerServiceTest extends AutoWiredBaseTest {
    @Autowired
    ExtCustomerService extCustomerService;

    @Test
    public void test() throws Exception {

        Firm firm = DTOUtils.newDTO(Firm.class);
        firm.setId(8L);

        ReflectionTestUtils.invokeMethod(AopTargetUtils.getTarget(this.extCustomerService), "findByCustomerIdList", new Object[]{Lists.newArrayList(529L, 589L), firm,});
        this.extCustomerService.querySellersByKeyWord("一号", firm);
    }
}
