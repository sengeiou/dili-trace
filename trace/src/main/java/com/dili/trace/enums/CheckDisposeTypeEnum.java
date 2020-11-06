package com.dili.trace.enums;

import one.util.streamex.StreamEx;

import java.util.Optional;

/**
 * Description:
 *
 * @author Lily.Huang
 * @date 2020/11/4
 */
public enum CheckDisposeTypeEnum {
    /**
     * 合格
     */
    PASS(0, "合格"),
    /**
     * 不合格
     */
    NO_PASS(1, "不合格"),
    ;

    private String name;
    private Integer code;

    CheckDisposeTypeEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public static Optional<CheckDisposeTypeEnum> fromCode(Integer code) {
        return StreamEx.of(CheckDisposeTypeEnum.values()).filterBy(CheckDisposeTypeEnum::getCode, code).findFirst();
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
