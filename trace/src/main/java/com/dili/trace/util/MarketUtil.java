package com.dili.trace.util;

import com.dili.trace.service.MarketService;

/**
 * Description:
 *
 * @date: 2020/10/14
 * @author: Lily
 */
public class MarketUtil {
    /** 市场默认值 */
    private static final Long MARKET_ID = 1L;

    public static Long returnMarket(){
        MarketService marketService = SpringbootUtil.getBean(MarketService.class);
        Long marketId = marketService.getCurrentLoginMarketId();
        return marketId;
    }
}
