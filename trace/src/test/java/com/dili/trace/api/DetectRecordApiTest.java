package com.dili.trace.api;

import com.dili.ss.retrofitful.annotation.RestfulScan;
import com.dili.trace.BaseTestWithouMVC;
import com.dili.uap.sdk.rpc.DataAuthRefRpc;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
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
//@Transactional
@Transactional(propagation = Propagation.NEVER)
@Rollback
@MapperScan(basePackages = {"com.dili.trace.dao", "com.dili.ss.dao", "com.dili.ss.uid.dao"})
@ComponentScan(basePackages = {"com.dili.ss", "com.dili.trace", "com.dili.common", "com.dili.commons", "com.dili.uap.sdk"})
@RestfulScan({"com.dili.trace.rpc", "com.dili.uap.sdk.rpc", "com.dili.bpmc.sdk.rpc"})
@EnableScheduling

@EnableAsync
@EnableFeignClients(basePackages = {"com.dili.assets.sdk.rpc"
        , "com.dili.customer.sdk.rpc"
        , "com.dili.trace.rpc"
        , "com.dili.bpmc.sdk.rpc"})
@Import(FeignClientsConfiguration.class)
@EnableDiscoveryClient

public class DetectRecordApiTest   {
    @MockBean
    ErrorAttributes attributes;
    @MockBean
    ServletContext servletContext;
    @SpyBean
    DataAuthRefRpc dataAuthRefRpc;


    @Autowired
    DetectRecordApi detectRecordApi;
    @Test
    public void ddd() {
        System.out.println("ddd");
    }
}
