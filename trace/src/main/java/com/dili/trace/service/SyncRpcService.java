package com.dili.trace.service;

import com.dili.uap.sdk.domain.User;

import java.util.List;

/**
 * 同步RPC
 * @author asa.lee
 */
public interface SyncRpcService {
    /**
     * 同步
     * @param marketCode
     * @return
     */
    List<User> syncRpcUserByMarketId(String marketCode);
}
