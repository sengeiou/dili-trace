package com.dili.trace.service;

import com.dili.ss.retrofitful.annotation.RestfulScan;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.dto.OperatorUser;
import com.dili.trace.enums.WeightUnitEnum;
import com.dili.trace.rpc.dto.RegCreateResultDto;
import com.dili.trace.rpc.dto.StockReductResultDto;
import com.dili.trace.rpc.service.ProductRpcService;
import com.dili.uap.sdk.domain.UserTicket;
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
import java.math.BigDecimal;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("dev")
@WebAppConfiguration("src/main/resources")
@EnableTransactionManagement
@Transactional(propagation = Propagation.NEVER)
@Rollback
@MapperScan(basePackages = {"com.dili.trace.dao", "com.dili.ss.dao", "com.dili.ss.uid.dao"})
@ComponentScan(basePackages = {"com.dili.ss", "com.dili.trace", "com.dili.common", "com.dili.commons", "com.dili.uap.sdk"})
@RestfulScan({"com.dili.trace.rpc", "com.dili.uap.sdk.rpc","com.dili.bpmc.sdk.rpc"})
@EnableFeignClients(basePackages = {"com.dili.assets.sdk.rpc","com.dili.trace.rpc","com.dili.bpmc.sdk.rpc"})
@Import(FeignClientsConfiguration.class)
@EnableDiscoveryClient
public class ProductRpcServiceTest {
    @MockBean
    ErrorAttributes attributes;
    @MockBean
    ServletContext servletContext;

    @Autowired
    ProductRpcService productRpcService;
    @Autowired
    BillService billService;

    /**
     * test
     */
    @Test
    public RegCreateResultDto createTest() {
//        RegisterBill bill = new RegisterBill();
//        bill.setMarketId(11L);
//        bill.setPlate("粤B23456");
//        bill.setBrandName("coco");
//        bill.setSpecName("箱");
//        bill.setUserId(12L);
//        bill.setName("测试新增库存");
//        bill.setProductId(43L);
//        bill.setProductName("梨子");
//        bill.setWeightUnit(WeightUnitEnum.JIN.getCode());
//        bill.setOriginName("广东深圳");
//        bill.setUnitPrice(BigDecimal.TEN);
//        bill.setWeight(BigDecimal.valueOf(120));

        RegisterBill bill = billService.get(158L);

        OperatorUser operatorUser = new OperatorUser(31L, "悟空");
        Optional<OperatorUser > opt = Optional.of(operatorUser);
        RegCreateResultDto result = productRpcService.create(bill, opt);
        return result;
    }

    @Test
    public void reduceTest() {
        RegCreateResultDto addRes = createTest();

        OperatorUser operatorUser = new OperatorUser(31L, "悟空");
        Optional<OperatorUser > opt = Optional.of(operatorUser);
        RegisterBill bill = billService.get(158L);
        bill.setWeight(BigDecimal.ONE);
        // 如何获取？
        Long bizId = 1L;
        StockReductResultDto stockReductResultDto = productRpcService.reduceByStockIds(bizId, bill, opt);
    }

}
