package com.dili.trace.glossary;

import java.util.stream.Stream;

public enum UpStreamTypeEnum {
    /**
     * 个人信息
     */
    USER(10, "个人"),
    /**
     * 企业
     */
    CORPORATE(20, "企业"),;

    private Integer code;
    private String name;

    UpStreamTypeEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public static UpStreamTypeEnum fromCode(Integer code) {
        return Stream.of(UpStreamTypeEnum.values()).filter(e -> e.getCode().equals(code)).findFirst().orElse(null);
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
