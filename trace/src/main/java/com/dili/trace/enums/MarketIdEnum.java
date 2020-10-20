package com.dili.trace.enums;

import one.util.streamex.StreamEx;

import java.util.Optional;

/**
 * @author asa.lee
 */

public enum MarketIdEnum {
    /**
     * 杭水市场
     */
    AQUATIC_TYPE(1, "杭水市场"),
    /**
     * 杭果市场
     */
    FRUIT_TYPE(2, "杭果市场"),
    ;

    private String name;
    private Integer code;

    MarketIdEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public static Optional<MarketIdEnum> fromCode(Integer code) {
        return StreamEx.of(MarketIdEnum.values()).filterBy(MarketIdEnum::getCode, code).findFirst();
    }

    public boolean equalsToCode(Integer code) {
        return this.getCode().equals(code);
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
