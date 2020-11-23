package com.dili.sg.common.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedisPool;

@Configuration
public class ManageRedisUtilConfiguration {
    @Value("${redis.maxIdle:500}")
    private Integer maxIdle;
    @Value("${redis.maxWaitMillis:1000}")
    private Integer maxWaitMillis;
    @Value("${redis.testOnBorrow:true}")
    private Boolean testOnBorrow;
    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.port}")
    private int port;

    

    @Bean("manageRedisUtil")
    public ManageRedisUtil createManageRedisUtilBean() {
        ManageRedisUtil manageRedisUtil = new ManageRedisUtil();
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);
        jedisPoolConfig.setTestOnBorrow(testOnBorrow);

        // 初始化一个redis连接
        List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();
        JedisShardInfo si = new JedisShardInfo(host, port);
        shards.add(si);

        // 初始化一个redis 集群
        ShardedJedisPool shardedJedisPool = new ShardedJedisPool(jedisPoolConfig, shards);
        manageRedisUtil.setShardedJedisPool(shardedJedisPool);
        return manageRedisUtil;
    }

}