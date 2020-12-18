package com.dili.trace.enums;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Optional;

/**
 * 报备单创建人类型。
 * @author Alvin.Li
 */
public enum CreatorRoleEnum {
    /**
     * 经营户
     */
    CUSTOMER(0, "经营户"),
    /**
     * 管理员
     */
    MANAGER(1, "管理员")
    ;

    private String name;
    private Integer code;

    CreatorRoleEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public static Optional<CreatorRoleEnum> fromCode(Integer code) {
        for (CreatorRoleEnum anEnum : CreatorRoleEnum.values()) {
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
        return CreatorRoleEnum.fromCode(code).map(CreatorRoleEnum::getName).orElse("");
    }
}
