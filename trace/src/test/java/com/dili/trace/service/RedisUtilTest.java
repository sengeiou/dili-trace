package com.dili.trace.service;

import java.util.concurrent.TimeUnit;

import com.dili.ss.redis.service.RedisUtil;
import com.dili.trace.AutoWiredBaseTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class RedisUtilTest extends AutoWiredBaseTest {

    @Autowired
    RedisUtil redisUtil;

    @Test
    public void dd() throws InterruptedException {
        this.redisUtil.set("redis_test", "123456", 10L, TimeUnit.SECONDS);
        Thread.sleep(1000 * 5);
        Object value = this.redisUtil.get("redis_test");
        System.out.println(value);
        String str = String.valueOf(value);
        System.out.println(str);
    }
}