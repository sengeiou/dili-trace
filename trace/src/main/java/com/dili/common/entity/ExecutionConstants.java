package com.dili.common.entity;

import com.dili.trace.service.MarketService;
import com.dili.uap.sdk.domain.Firm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 系统通用常量
 */
@Component
public class ExecutionConstants {

    //市场Code
    @Value("${market.code}")
    private String marketCode;

    @Autowired
    private MarketService marketService;

    //未登陆
    public static final String NO_LOGIN="401";

    //WAITING_DISABLED KEY 等待被禁用的用户
    public static final String WAITING_DISABLED_USER_PREFIX="WAITING_DISABLED_USERS";

    //系统code
    public final static String SYSTEM_CODE = "trace";

    // TODO:值待定
    public static final String REDIS_SYSTEM_VERCODE_PREIX="";

    public String getMarketCode() {
        Firm currentMarket = marketService.getCurrentMarket();
        return currentMarket.getCode();
    }
}
