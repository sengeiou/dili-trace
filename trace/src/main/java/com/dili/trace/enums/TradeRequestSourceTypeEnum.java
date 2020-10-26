package com.dili.trace.enums;

import one.util.streamex.StreamEx;

import java.util.Optional;

/**
 * @author asa.lee
 */
public enum TradeRequestSourceTypeEnum {

    /**
     * 系统新增
     */
    SYSTEM_CREATE(1, "系统新增"),
    /**
     * 第三方接口获取-杭果
     */
    THIRD_HANGGUO(2, "杭果接口"),
    ;

    private String name;
    private Integer code;

    TradeRequestSourceTypeEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public static Optional<TradeRequestSourceTypeEnum> fromCode(Integer code) {
        return StreamEx.of(TradeRequestSourceTypeEnum.values()).filterBy(TradeRequestSourceTypeEnum::getCode, code).findFirst();
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
