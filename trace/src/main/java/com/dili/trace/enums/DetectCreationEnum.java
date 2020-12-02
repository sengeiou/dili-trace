package com.dili.trace.enums;

import java.util.Optional;

public enum DetectCreationEnum {
    /**
     * 自动
     */
    AUTO(10, "自动"),
    /**
     * 手动
     */
    MANULLY(20, "手动"),

    ;


    private String name;
    private Integer code;

    DetectCreationEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public static Optional<DetectCreationEnum> fromCode(Integer code) {
        for (DetectCreationEnum anEnum : DetectCreationEnum.values()) {
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
