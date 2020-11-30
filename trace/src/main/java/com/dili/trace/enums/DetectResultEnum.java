package com.dili.trace.enums;

import java.util.Optional;

public enum DetectResultEnum {
    /**
     * 无
     */
    NONE(0, ""),
    /**
     * 合格
     */
    PASSED(1, "合格"),
    /**
     * 不合格
     */
    FAILED(2, "不合格"),
    ;


    private String name;
    private Integer code;

    DetectResultEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public static Optional<DetectResultEnum> fromCode(Integer code) {
        for (DetectResultEnum anEnum : DetectResultEnum.values()) {
            if (anEnum.getCode().equals(code)) {
                return Optional.ofNullable(anEnum);
            }
        }
        return Optional.empty();
    }

    public Boolean equalsToCode(Integer code) {
        return this.getCode().equals(code);
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
