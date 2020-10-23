package com.dili.trace.util;

import com.dili.trace.domain.Market;
import com.dili.trace.service.MarketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
        return MARKET_ID;
    }
}
