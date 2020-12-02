package com.dili.trace.enums;

import java.util.Optional;

public enum DetectModeEnum {

    /**
     * 无
     */
    NONE(1, ""),
    /**
     * 复检
     */
    RECHECK(2, "主动送检"),
    /**
     * 抽检
     */
    SAMPLE_CHECK(3, "采样检测"),
    ;


    private String name;
    private Integer code;

    DetectModeEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public static Optional<DetectModeEnum> fromCode(Integer code) {
        for (DetectModeEnum anEnum : DetectModeEnum.values()) {
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
