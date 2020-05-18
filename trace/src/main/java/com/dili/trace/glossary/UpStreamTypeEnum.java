package com.dili.trace.glossary;

public enum UpStreamTypeEnum {
    /**
     * 个人信息
     */
    USER(10, "个人"),
    /**
     * 企业
     */
    CORPORATE(20, "企业"),
    ;

    private Integer code;
    private String name;

    UpStreamTypeEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
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
