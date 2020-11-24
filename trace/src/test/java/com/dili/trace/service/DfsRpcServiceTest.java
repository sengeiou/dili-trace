package com.dili.trace.service;

import cn.hutool.http.HttpUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.IOException;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("dev")
@WebAppConfiguration("src/main/resources")
//@TestExecutionListeners(mergeMode = MergeMode.MERGE_WITH_DEFAULTS, inheritListeners = true, value = {
//		MockitoDependencyInjectionTestExecutionListener.class }) // ,
// DependencyInjectionTestExecutionListener.class
// })
@EnableTransactionManagement
//@Transactional
@Transactional(propagation = Propagation.NEVER)
@Rollback
@EnableFeignClients(basePackages = {"com.dili.assets.sdk.rpc","com.dili.trace.rpc"})
@Import(FeignClientsConfiguration.class)
@EnableDiscoveryClient
public class DfsRpcServiceTest {
    @MockBean
    ErrorAttributes attributes;
    @MockBean
    ServletContext servletContext;

    @Autowired
    DfsRpcService dfsRpcService;

    @Test
    public void uploadfile() throws IOException {
        File file = new File("e:/abc.txt");
        String fileId = this.dfsRpcService.fileUpload(file).orElse(null);
        System.out.println(fileId);
        String url="http://gateway.diligrp.com:8285/dili-dfs/file/view/"+fileId;
        String content=HttpUtil.get(url);

    }
}
