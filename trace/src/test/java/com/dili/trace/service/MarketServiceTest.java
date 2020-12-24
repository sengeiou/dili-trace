package com.dili.trace.service;

import com.dili.ss.retrofitful.annotation.RestfulScan;
import com.dili.trace.enums.MarketEnum;
import com.dili.uap.sdk.domain.Firm;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
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

import javax.servlet.ServletContext;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("dev")
@WebAppConfiguration("src/main/resources")
@EnableTransactionManagement
@Transactional(propagation = Propagation.NEVER)
@Rollback
//@MapperScan(basePackages = {"com.dili.trace.dao", "com.dili.ss.dao", "com.dili.ss.uid.dao"})
//@ComponentScan(basePackages = {"com.dili.ss", "com.dili.trace", "com.dili.common", "com.dili.commons", "com.dili.uap.sdk"})
//@RestfulScan({"com.dili.trace.rpc", "com.dili.uap.sdk.rpc","com.dili.bpmc.sdk.rpc"})
@EnableFeignClients(basePackages = {"com.dili.assets.sdk.rpc","com.dili.trace.rpc","com.dili.bpmc.sdk.rpc"})
@Import(FeignClientsConfiguration.class)
@EnableDiscoveryClient
public class MarketServiceTest {
    @MockBean
    ErrorAttributes attributes;
    @MockBean
    ServletContext servletContext;

    @Autowired
    MarketService marketService;

    @Test
    public void getMarketByCode() {
        Firm marketByCode = this.marketService.getMarketByCode(MarketEnum.HZSG);
        System.out.println(marketByCode.toString());
    }

}