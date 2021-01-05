package com.dili.trace.enums;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Optional;

public enum DetectStatusEnum {
    /**
     * 待预约
     */
    NONE(0, "待预约"),

    /**
     * 待接单
     */
    WAIT_DESIGNATED(10, "待接单"),
    /**
     * 待采样
     */
    WAIT_SAMPLE(20, "待采样"),
    /**
     * 待检测
     */
    WAIT_DETECT(30, "待检测"),

    /**
     * 检测中
     */
    DETECTING(40, "检测中"),
    /**
     * 已检测
     */
    FINISH_DETECT(50, "已检测"),

    /**
     * 已退回
     */
    RETURN_DETECT(60, "已退回"),
    ;


    private String name;
    private Integer code;

    DetectStatusEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public static Optional<DetectStatusEnum> fromCode(Integer code) {
        for (DetectStatusEnum anEnum : DetectStatusEnum.values()) {
            if (anEnum.getCode().equals(code)) {
                return Optional.ofNullable(anEnum);
            }
        }
        return Optional.empty();
    }

    public static String name(Integer code) {
        return DetectStatusEnum.fromCode(code).map(DetectStatusEnum::getName).orElse("");
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
