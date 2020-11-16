package com.dili.common.entity;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ExecutionConstants {

    //市场Code
    @Value("${market.code}")
    private String marketCode;

    //未登陆
    public static final String NO_LOGIN="401";

    //WAITING_DISABLED KEY 等待被禁用的用户
    public static final String WAITING_DISABLED_USER_PREFIX="WAITING_DISABLED_USERS";

    //系统code
    public final static String SYSTEM_CODE = "trace";

    public String getMarketCode() {
        return marketCode;
    }
}
