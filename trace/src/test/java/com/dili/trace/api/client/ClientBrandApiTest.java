 package com.dili.trace.api.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import com.dili.common.entity.LoginSessionContext;
import com.dili.ss.domain.BaseOutput;
import com.dili.trace.AutoWiredBaseTest;
import com.dili.trace.api.input.BrandInputDto;
import com.dili.trace.api.output.BrandOutputDto;
import com.dili.trace.domain.Brand;
import com.dili.trace.dto.OperatorUser;
import com.dili.trace.service.BrandService;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import one.util.streamex.StreamEx;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

 @EnableDiscoveryClient
public class ClientBrandApiTest extends AutoWiredBaseTest {
    @Autowired
    ClientBrandApi clientBrandApi;
    @Autowired
    BrandService brandService;
    @MockBean
    LoginSessionContext sessionContext;


    private Brand createBrand() {
        Brand brand = new Brand();
        brand.setUserId(1L);
        brand.setBrandName("精品一号");
        this.brandService.insertSelective(brand);
        return brand;
    }

}