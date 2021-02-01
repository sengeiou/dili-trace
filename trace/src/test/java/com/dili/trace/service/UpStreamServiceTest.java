package com.dili.trace.service;

import com.dili.ss.domain.BaseOutput;
import com.dili.trace.AutoWiredBaseTest;
import com.dili.trace.dto.OperatorUser;
import com.dili.trace.dto.UpStreamDto;
import com.dili.trace.glossary.UpStreamSourceEnum;
import com.dili.trace.glossary.UpStreamTypeEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import java.util.Arrays;
import java.util.Optional;

@EnableDiscoveryClient
public class UpStreamServiceTest extends AutoWiredBaseTest {
    @Autowired
    UpStreamService upStreamService;

    @Test
    public void addDeleteAddUpStream() {
        UpStreamDto input = new UpStreamDto();
        input.setTelphone("13333333333");
        input.setName("test");
        input.setUserIds(Arrays.asList(100L));
        input.setUpORdown(UpStreamSourceEnum.DOWN.getCode());
        input.setUpstreamType(UpStreamTypeEnum.USER.getCode());
        OperatorUser operatorUser = new OperatorUser(0L, "system");
        BaseOutput baseOutput = this.upStreamService.addUpstream(input, operatorUser, false);
        Long id = (Long) baseOutput.getData();

        this.upStreamService.deleteUpstream(100L, id);
        this.upStreamService.addUpstream(input, operatorUser, true);


    }
}
