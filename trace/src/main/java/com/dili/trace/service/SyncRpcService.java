package com.dili.trace.service;

import com.dili.uap.sdk.domain.User;
import org.springframework.scheduling.annotation.Async;

import java.util.List;

/**
 * 同步RPC
 *
 * @author asa.lee
 */
public interface SyncRpcService {
    /**
     * 同步市场用户信息
     *
     * @param marketCode
     * @return
     */
    void syncRpcUserByMarketId(String marketCode);

    /**
     * 同步用户信息
     *
     * @param userId
     * @return
     */
    @Async
    void syncRpcUserByUserId(Long userId);

    /**
     * 同步用户信息
     *
     * @param userIds
     * @return
     */
    void syncRpcUserByUserIds(List<String> userIds);

    /**
     * 测试
     *
     * @throws InterruptedException
     */
    @Async
    void testAsync() throws InterruptedException;


}
