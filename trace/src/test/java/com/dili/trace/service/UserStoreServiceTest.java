package com.dili.trace.service;

import com.dili.trace.AutoWiredBaseTest;
import com.dili.trace.domain.UserStore;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import java.util.List;

@EnableDiscoveryClient
public class UserStoreServiceTest extends AutoWiredBaseTest {
    @Autowired
    UserStoreService userStoreService;

    @Test
    public void test() {

        List<UserStore> list = this.userStoreService.listUserStoreByKeyword(8L, "悟空");
        System.out.println(list);
    }
}
