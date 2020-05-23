package com.dili.trace.glossary;

import java.util.stream.Stream;

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
    public static UserTypeEnum fromCode(Integer code) {
        return Stream.of(UserTypeEnum.values()).filter(e -> e.getCode().equals(code)).findFirst().orElse(null);
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
