package com.dili.trace.enums;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Optional;

public enum DetectResultEnum {
    /**
     * 无
     */
    NONE(0, "无"),
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
    public static String name(Integer code) {
        return DetectResultEnum.fromCode(code).map(DetectResultEnum::getName).orElse("");
    }

    public Boolean equalsToCode(Integer code) {
        return this.getCode().equals(code);
    }
    @JsonValue
    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
