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
    IS_SHOW("1", "显示"),
    /**
     * 不显示商品
     */
    NOT_SHOW("2", "不显示商品"),
    ;

    private String name;
    private String code;

    CategoryIsShowEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static Optional<CategoryIsShowEnum> fromCode(String code) {
        return StreamEx.of(CategoryIsShowEnum.values()).filterBy(CategoryIsShowEnum::getCode, code).findFirst();
    }

    public boolean equalsToCode(String code) {
        return this.getCode().equals(code);
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
