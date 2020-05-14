package com.dili.common.config;

import redis.clients.jedis.ShardedJedisPool;

public class ManageRedisUtil {
    private ShardedJedisPool shardedJedisPool;

    /**
     * @return ShardedJedisPool return the shardedJedisPool
     */
    public ShardedJedisPool getShardedJedisPool() {
        return shardedJedisPool;
    }

    /**
     * @param shardedJedisPool the shardedJedisPool to set
     */
    public void setShardedJedisPool(ShardedJedisPool shardedJedisPool) {
        this.shardedJedisPool = shardedJedisPool;
    }

}