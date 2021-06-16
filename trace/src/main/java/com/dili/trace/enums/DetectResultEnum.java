package com.dili.trace.enums;

import com.dili.common.exception.TraceBizException;
import com.fasterxml.jackson.annotation.JsonValue;
import one.util.streamex.StreamEx;

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
    public static DetectResultEnum fromCodeOrEx(Integer code) {
        return StreamEx.of(DetectResultEnum.values()).filterBy(DetectResultEnum::getCode, code).findFirst().orElseThrow(() -> {
            return new TraceBizException("检测结果错误");
        });
    }
    public static String name(Integer code) {
        return DetectResultEnum.fromCode(code).map(DetectResultEnum::getName).orElse("");
    }

    public Boolean equalsToCode(Integer code) {
        return this.getCode().equals(code);
    }
    @JsonValue
    public Integer getCode() {
        return this.code;
    }

    public String getName() {
        return this.name;
    }
}
