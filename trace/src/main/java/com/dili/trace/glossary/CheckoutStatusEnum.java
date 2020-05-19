package com.dili.trace.glossary;

import java.util.stream.Stream;

public enum CheckoutStatusEnum {
    /**
     * 通过
     */
    ALLOWED(10, "通过"),
    /**
     * 不通过
     */
    NOTALLOWED(20, "不通过"),;

    private Integer code;
    private String desc;

    CheckoutStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    public static CheckoutStatusEnum fromCode(Integer code) {
        return Stream.of(CheckoutStatusEnum.values()).filter(e -> e.getCode().equals(code)).findFirst().orElseGet(null);
    }
    public boolean equalsCode(Integer code) {
        return this.getCode().equals(code);

    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
