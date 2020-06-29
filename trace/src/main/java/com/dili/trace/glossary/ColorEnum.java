package com.dili.trace.glossary;

import java.util.stream.Stream;

public enum ColorEnum {
    /**
     * 黑色
     */
    BLACK(0, "黑色"),
    /**
     * 绿色
     */
    GREEN(10, "绿色"),
    /**
     * 黄色
     */
    YELLOW(20, "黄色"),
    /**
     * 红色
     */
    RED(30, "红色"),;

    private Integer code;
    private String desc;

    ColorEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static ColorEnum fromCode(Integer code) {
        return Stream.of(ColorEnum.values()).filter(e -> e.getCode().equals(code)).findFirst().orElse(null);
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
