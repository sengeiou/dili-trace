package com.dili.sg.trace.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 全局变量
 */
@Service
public class GlobalVarService {
    @Value("${market.id}")
    private Long marketId;

    /**
     * 返回市场id
     * @return
     */
    public Long getMarketId() {
        return marketId;
    }
}
