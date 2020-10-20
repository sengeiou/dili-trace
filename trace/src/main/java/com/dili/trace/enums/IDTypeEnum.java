package com.dili.trace.enums;

import one.util.streamex.StreamEx;

import java.util.Optional;

/**
 * @author asa.lee
 */

public enum IDTypeEnum {

    /**
     * 身份证
     */
    ID_CARD(1, "身份证"),
    /**
     * 驾驶证
     */
    DRIVER_CERTIFICATE(2, "驾驶证"),
    /**
     * 军人证
     */
    MILITARY_CERTIFICATE(3, "军人证"),
    /**
     * 营业执照
     */
    LISCENS_CERTIFICATE(4, "营业执照"),
    /**
     * 其他
     */
    OTHER(5, "其他"),
    /**
     * 不记名
     */
    ANONYMOUS(6, "不记名"),
    ;

    private String name;
    private Integer code;

    IDTypeEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public static Optional<IDTypeEnum> fromCode(Integer code) {
        return StreamEx.of(IDTypeEnum.values()).filterBy(IDTypeEnum::getCode, code).findFirst();
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
