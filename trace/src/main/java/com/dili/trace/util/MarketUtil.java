package com.dili.trace.util;

import com.dili.trace.service.MarketService;
import com.dili.trace.service.UapRpcService;

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
        UapRpcService marketService = SpringbootUtil.getBean(UapRpcService.class);
        return marketService.getCurrentFirm().get().getId();
    }
}
