package com.dili.trace.enums;

import one.util.streamex.StreamEx;

import java.util.Optional;

/**
 * Description:
 *
 * @author Lily.Huang
 * @date 2020/10/30
 */
public enum CheckTypeEnum {
    /**
     * 定性
     */
    QUALITATIVE(1, "定性"),
    /**
     * 定量
     */
    QUANTITATIVE(2, "定量"),
    ;

    private String name;
    private Integer code;

    CheckTypeEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public static Optional<CheckTypeEnum> fromCode(Integer code) {
        return StreamEx.of(CheckTypeEnum.values()).filterBy(CheckTypeEnum::getCode, code).findFirst();
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
