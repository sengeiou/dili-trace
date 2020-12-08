package com.dili.trace.service;


import com.dili.ss.retrofitful.annotation.RestfulScan;
import com.dili.trace.AutoWiredBaseTest;
import com.dili.trace.domain.DetectRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.spring.annotation.MapperScan;

import java.util.List;


@Import(FeignClientsConfiguration.class)
@EnableDiscoveryClient
@Rollback
public class DetectRequestServiceTest extends AutoWiredBaseTest {

    @Autowired
    DetectRequestService detectRequestService;

    @Test
    @Transactional
    public void selectRequestForDetect() {
        List<DetectRequest> list = this.detectRequestService.selectRequestForDetect("wangguofeng", 10, 8L);
        Assertions.assertNotNull(list);
    }

}
