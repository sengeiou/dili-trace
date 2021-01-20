package com.dili.trace.service;

import com.dili.trace.AutoWiredBaseTest;
import com.dili.trace.dto.DetectTaskApiOutputDto;
import com.dili.trace.dto.TaskGetParam;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;

import java.util.List;

@Import(FeignClientsConfiguration.class)
@EnableDiscoveryClient
@Rollback
public class DetectTaskServiceTest extends AutoWiredBaseTest {
    @Autowired
    DetectTaskService detectTaskService;

    @Test
    public void findByExeMachineNo() {
        TaskGetParam taskGetParam = new TaskGetParam();
        taskGetParam.setMarketId(8L);
        taskGetParam.setExeMachineNo("test");
        taskGetParam.setPageSize(2);
        List<DetectTaskApiOutputDto> list = this.detectTaskService.findByExeMachineNo(taskGetParam);
        System.out.println(list);
    }
}
