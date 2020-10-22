package com.dili.trace.enums;

import one.util.streamex.StreamEx;

import java.util.Optional;

/**
 * @author asa.lee
 */

public enum CheckOrderReportFlagEnum {

    /**
     * 未处理
     */
    UNTREATED(-1, "未处理"),
    /**
     * 已处理
     */
    PROCESSED(1, "已处理"),
    /**
     * 已上报
     */
    REPORTED(2, "已上报"),
    ;

    private String name;
    private Integer code;

    CheckOrderReportFlagEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public static Optional<CheckOrderReportFlagEnum> fromCode(Integer code) {
        return StreamEx.of(CheckOrderReportFlagEnum.values()).filterBy(CheckOrderReportFlagEnum::getCode, code).findFirst();
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
