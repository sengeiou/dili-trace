package com.dili.trace.enums;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Optional;

/**
 * @author asa.lee
 */

public enum SpotTypeStatusEnum {
    /**
     * 抽检检测
     */
    NORMAL(1, "抽检检测"),

    /**
     * 人工录入抽检结果
     */
    WAIT_DESIGNATED(2, "人工录入抽检结果"),

    ;


    private String name;
    private Integer code;

    SpotTypeStatusEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public static Optional<SpotTypeStatusEnum> fromCode(Integer code) {
        for (SpotTypeStatusEnum anEnum : SpotTypeStatusEnum.values()) {
            if (anEnum.getCode().equals(code)) {
                return Optional.ofNullable(anEnum);
            }
        }
        return Optional.empty();
    }

    public static String name(Integer code) {
        return SpotTypeStatusEnum.fromCode(code).map(SpotTypeStatusEnum::getName).orElse("");
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
