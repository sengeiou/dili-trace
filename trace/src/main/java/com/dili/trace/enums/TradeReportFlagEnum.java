package com.dili.trace.enums;

import one.util.streamex.StreamEx;

import java.util.Optional;

/**
 * @author asa.lee
 */

public enum TradeReportFlagEnum {

    /**
     * 1上报
     */
    NEED_REPORT(1, "上报"),
    /**
     * 不上报
     */
    UNREPORTED(-1, "不上报"),
    ;

    private String name;
    private Integer code;

    TradeReportFlagEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public static Optional<TradeReportFlagEnum> fromCode(Integer code) {
        return StreamEx.of(TradeReportFlagEnum.values()).filterBy(TradeReportFlagEnum::getCode, code).findFirst();
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
