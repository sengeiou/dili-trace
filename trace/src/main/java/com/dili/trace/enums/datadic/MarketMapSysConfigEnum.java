package com.dili.trace.enums.datadic;

/**
 * @author Alvin
 * @version 1.0
 * @ClassName MarketMapSysConfigEnum
 * @Description
 * 溯源市场映射数据字典配置
 * @createTime 2020年11月26日 10:02:00
 */
public enum MarketMapSysConfigEnum {

    DD_CODE("trace_market", "数字字典key"),

    HZSC("HZSC", "杭州水产市场"),

    HZSG("HZSG", "杭州水果市场"),

    SDSG("SDSG", "山东寿光市场")
    ;

    MarketMapSysConfigEnum(String name) {
        this.name = name;
    }
    MarketMapSysConfigEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }
    private String name;
    private String code;

    public String getName() {
        return name;
    }
    public String getCode() {
        return code;
    }
}
