package com.dili.trace.enums;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Optional;

public enum DetectTypeEnum {
    /**
     * 无
     */
    NEW(10, "无"),
    /**
     * 初检
     */
    INITIAL_CHECK(20, "初检"),
    /**
     * 复检
     */
    RECHECK(30, "复检"),
    /**
     * 抽检
     */
    SPOT_CHECK(40, "抽检"),



    /**
     * 其他
     */
    OTHERS(9999, "其他"),
    ;


    private String name;
    private Integer code;

    DetectTypeEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public static Optional<DetectTypeEnum> fromCode(Integer code) {
        for (DetectTypeEnum anEnum : DetectTypeEnum.values()) {
            if (anEnum.getCode().equals(code)) {
                return Optional.ofNullable(anEnum);
            }
        }
        return Optional.empty();
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

    public static String name(Integer code) {
        return DetectTypeEnum.fromCode(code).map(DetectTypeEnum::getName).orElse("");
    }
}
