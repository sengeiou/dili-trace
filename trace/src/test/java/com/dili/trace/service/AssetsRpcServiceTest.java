package com.dili.trace.service;

import cn.hutool.http.HttpUtil;
import com.dili.assets.sdk.dto.CarTypeDTO;
import com.dili.assets.sdk.dto.CarTypePublicDTO;
import com.dili.assets.sdk.dto.CusCategoryDTO;
import com.dili.assets.sdk.dto.CusCategoryQuery;
import com.dili.ss.retrofitful.annotation.RestfulScan;
import com.dili.trace.rpc.service.CarTypeRpcService;
import one.util.streamex.StreamEx;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
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
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
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


//处理事务支持
@EnableAspectJAutoProxy(proxyTargetClass = true)
@MapperScan(basePackages = {"com.dili.ss.uid.mapper","com.dili.trace.dao", "com.dili.ss.dao"})
@ComponentScan(basePackages = {"com.dili.ss.uid","com.dili.ss", "com.dili.trace", "com.dili.common", "com.dili.commons", "com.dili.uap.sdk"})
@RestfulScan({"com.dili.ss.uid","com.dili.trace.rpc", "com.dili.uap.sdk.rpc", "com.dili.bpmc.sdk.rpc"})
//@DTOScan({"com.dili.trace","com.dili.ss"})
//@Import(DynamicRoutingDataSourceRegister.class)
@EnableScheduling

@EnableAsync
@EnableFeignClients(basePackages = {"com.dili.ss.uid","com.dili.assets.sdk.rpc"
        , "com.dili.customer.sdk.rpc"
        , "com.dili.trace.rpc"
        , "com.dili.bpmc.sdk.rpc"})
@Import(FeignClientsConfiguration.class)
@ServletComponentScan
@EnableDiscoveryClient
public class AssetsRpcServiceTest {
    @MockBean
    ErrorAttributes attributes;
    @MockBean
    ServletContext servletContext;

    @Autowired
    AssetsRpcService asetsRpcService;
    @Autowired
    CarTypeRpcService carTypeRpcService;

    @Test
    public void listCusCategory() throws IOException {
        CusCategoryQuery query = new CusCategoryQuery();
        query.setKeyword("成都");
        List<CusCategoryDTO> list = this.asetsRpcService.listCusCategory(query, null);
        System.out.println(list);

    }

    @Test
    public void listCarType() {
        Map<Long, String> carTypeMap = StreamEx.ofNullable(this.carTypeRpcService.listCarType()).flatCollection(Function.identity())
                .mapToEntry(CarTypeDTO::getId, CarTypeDTO::getName).toMap();
        System.out.println(carTypeMap);
    }

}
