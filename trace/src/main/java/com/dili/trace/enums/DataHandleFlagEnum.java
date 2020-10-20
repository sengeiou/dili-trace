package com.dili.trace.enums;

import one.util.streamex.StreamEx;

import java.util.Optional;

/**
 * @author asa.lee
 */

public enum DataHandleFlagEnum {

    /**
     * 待处理
     */
    PENDING_HANDLE(1, "待处理"),
    /**
     * 已处理
     */
    PROCESSED(2, "已处理"),
    /**
     * 无需处理
     */
    UN_NEED_HANDLE(3, "无需处理"),
    ;

    private String name;
    private Integer code;

    DataHandleFlagEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public static Optional<DataHandleFlagEnum> fromCode(Integer code) {
        return StreamEx.of(DataHandleFlagEnum.values()).filterBy(DataHandleFlagEnum::getCode, code).findFirst();
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
