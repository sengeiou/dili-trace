package com.dili.trace.service;

import com.dili.ss.base.BaseService;
import com.dili.trace.domain.Market;
import com.dili.uap.sdk.domain.Firm;

/**
 * @author Lily
 */
public interface MarketService extends BaseService<Market, Long> {
    /**
     *
     * @Author guzman.liu
     * @Description
     * 获取当前市场的ID
     * @Date 2020/11/26 14:23
     */
    Long getCurrentLoginMarketId();

    /**
     *
     * @Author guzman.liu
     * @Description
     * 获取当前市场的所有信息
     * @Date 2020/11/26 14:23
     */
    public Firm getCurrentMarket();
}
