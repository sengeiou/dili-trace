package com.dili.trace.service;

import com.dili.ss.base.BaseService;
import com.dili.trace.domain.TradePushLog;
import com.dili.trace.dto.TradePushInputDto;

public interface TradePushService extends BaseService<TradePushLog,Long> {
    /**
     * 上下架
     * @param tradePushLog
     */
    void tradePush(TradePushLog tradePushLog);
}
