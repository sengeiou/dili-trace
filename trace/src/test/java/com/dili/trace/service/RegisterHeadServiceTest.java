package com.dili.trace.service;

import com.dili.ss.domain.BasePage;
import com.dili.trace.AutoWiredBaseTest;
import com.dili.trace.domain.RegisterHead;
import com.dili.trace.dto.RegisterHeadDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import java.math.BigDecimal;
@EnableDiscoveryClient
public class RegisterHeadServiceTest extends AutoWiredBaseTest {
    @Autowired
    RegisterHeadService registerHeadService;

    @Test
    public void listPageApi() {
        RegisterHeadDto input = new RegisterHeadDto();
        input.setMinRemainWeight(BigDecimal.ZERO);
        BasePage<RegisterHead> list = this.registerHeadService.listPageApi(input);
        Assertions.assertNotNull(list);
    }
}
