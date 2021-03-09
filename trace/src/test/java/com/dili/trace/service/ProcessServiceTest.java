package com.dili.trace.service;

import com.dili.commons.glossary.YesOrNoEnum;
import com.dili.trace.AutoWiredBaseTest;
import com.dili.trace.domain.ProcessConfig;
import com.dili.trace.domain.RegisterBill;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import java.util.Optional;

@EnableDiscoveryClient
public class ProcessServiceTest extends AutoWiredBaseTest {
    @SpyBean
    ProcessService processService;
    @Autowired
    BillService billService;
    @SpyBean
    ProcessConfigService processConfigService;

    @Test
    public void afterCreateBill() {

        Mockito.doNothing().when(this.processService).afterCreateBill(Mockito.anyLong(), Mockito.anyLong(), Mockito.any());
        Mockito.doAnswer((InvocationOnMock invocation) -> {
            Long marketId = invocation.getArgument(0);
            ProcessConfig processConfig = new ProcessConfig();
            processConfig.setIsAutoVerifyPassed(YesOrNoEnum.NO.getCode());
            processConfig.setIsManullyCheckIn(YesOrNoEnum.YES.getCode());

            processConfig.setCanDoCheckInWithoutWeight(YesOrNoEnum.NO.getCode());

            processConfig.setMarketId(marketId);
            return processConfig;
        }).when(this.processConfigService).findByMarketId(Mockito.anyLong());

        Long billId = super.baseCreateRegisterBill();
        RegisterBill bill = this.billService.get(billId);

        Mockito.doCallRealMethod().when(this.processService).afterCreateBill(Mockito.anyLong(), Mockito.anyLong(), Mockito.any());
        this.processService.afterCreateBill(billId, bill.getMarketId(), Optional.empty());
    }
}
