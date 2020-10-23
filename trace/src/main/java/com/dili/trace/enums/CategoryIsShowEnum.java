package com.dili.trace.enums;

import one.util.streamex.StreamEx;

import java.util.Optional;

/**
 * @author asa.lee
 */

public enum CategoryIsShowEnum {

    /**
     * 显示商品
     */
    IS_SHOW(1, "显示"),
    /**
     * 不显示商品
     */
    NOT_SHOW(2, "不显示商品"),
    ;

    private String name;
    private Integer code;

    CategoryIsShowEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public static Optional<CategoryIsShowEnum> fromCode(Integer code) {
        return StreamEx.of(CategoryIsShowEnum.values()).filterBy(CategoryIsShowEnum::getCode, code).findFirst();
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
