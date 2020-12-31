package com.dili.trace.enums;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Optional;

/**
 * 检测记录结果
 * @author Alvin.Li
 */
public enum DetectRecordStateEnum {
    /**
     * 合格
     */
    QUALIFIED(1, "合格"),

    /**
     * 不合格
     */
    UNQUALIFIED(2, "不合格"),
    ;


    private String name;
    private Integer code;

    DetectRecordStateEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public static Optional<DetectRecordStateEnum> fromCode(Integer code) {
        for (DetectRecordStateEnum anEnum : DetectRecordStateEnum.values()) {
            if (anEnum.getCode().equals(code)) {
                return Optional.ofNullable(anEnum);
            }
        }
        return Optional.empty();
    }

    public static String name(Integer code) {
        return DetectRecordStateEnum.fromCode(code).map(DetectRecordStateEnum::getName).orElse("");
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
