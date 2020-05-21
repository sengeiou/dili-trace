package com.dili.trace.glossary;

public enum UserTypeEnum {
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
    private String desc;

    UserTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
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
