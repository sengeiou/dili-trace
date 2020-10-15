package com.dili.trace.util;

import com.dili.trace.domain.Market;
import com.dili.trace.service.MarketService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Description:
 *
 * @date: 2020/10/14
 * @author: Lily
 */
public class MarketUtil {
    /** 品类参考价 参考价规则  */
    private static final Long MARKET_ID = 1L;

    @Autowired
    private static MarketService marketService;

    public static Long returnMarket(){
        return MARKET_ID;
    }

    public static List<Market> getAllMarket(){
        return marketService.listByExample(new Market());
    }
}
