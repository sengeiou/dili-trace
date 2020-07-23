package com.dili.trace.service;

import com.dili.common.service.RedisService;
import com.dili.trace.AutoWiredBaseTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class RedisServiceTest extends AutoWiredBaseTest {

    @Autowired
    RedisService redisService;

    @Test
    public void getExpire() {

        long expire = this.redisService.getExpire("test");
        System.out.println(expire);
    }
}