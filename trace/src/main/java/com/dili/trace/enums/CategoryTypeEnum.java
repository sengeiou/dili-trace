package com.dili.trace.enums;

import one.util.streamex.StreamEx;

import java.util.Optional;

/**
 * @author asa.lee
 */

public enum CategoryTypeEnum {

    /**
     * 正常商品
     */
    NONE(1, "正常商品"),
    /**
     * 检测商品
     */
    SUPPLEMENT(2, "检测商品"),
    ;

    private String name;
    private Integer code;

    CategoryTypeEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public static Optional<CategoryTypeEnum> fromCode(Integer code) {
        return StreamEx.of(CategoryTypeEnum.values()).filterBy(CategoryTypeEnum::getCode, code).findFirst();
    }

    public boolean equalsToCode(Integer code) {
        return this.getCode().equals(code);
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
