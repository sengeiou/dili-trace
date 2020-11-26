package com.dili.trace.enums;

import one.util.streamex.StreamEx;

import java.util.Optional;

/**
 * @author asa.lee
 */
public enum MarketEnum {

    HZSC("HZSC", "杭州水产市场"),

    HZSG("HZSG", "杭州水果市场"),

    SDSG("SDSG", "山东寿光市场")
    ;

    private String name;
    private String code;

    MarketEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static Optional<MarketEnum> fromCode(Integer code) {
        return StreamEx.of(MarketEnum.values()).filterBy(MarketEnum::getCode, code).findFirst();
    }

    public boolean equalsToCode(Integer code) {
        return this.getCode().equals(code);
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
