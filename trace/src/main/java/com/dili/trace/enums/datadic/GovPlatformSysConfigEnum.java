package com.dili.trace.enums.datadic;

/**
 * @author Guzman
 * @version 1.0
 * @ClassName GovPlatformSysConfigEnum
 * @Description
 * 连接融食安系统数据字典配置
 * @createTime 2020年11月26日 10:02:00
 */
public enum GovPlatformSysConfigEnum {



    DD_CODE("rsa_config_info"),
    URL("url"),
    MARKET_ID("marketId"),
    APP_ID("appId"),
    APP_SECRET("appSecret")
    ;

    GovPlatformSysConfigEnum(String name) {
        this.name = name;
    }
    private String name;

    public String getName() {
        return name;
    }
}
