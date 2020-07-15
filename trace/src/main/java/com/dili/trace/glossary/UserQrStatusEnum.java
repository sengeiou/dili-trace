package com.dili.trace.glossary;

import java.util.stream.Stream;

public enum UserQrStatusEnum {
    /**
     * 黑色
     */
    BLACK(0, "黑色", "000000"),
    /**
     * 绿色
     */
    GREEN(10, "绿色", "17a365"),
    /**
     * 黄色
     */
    YELLOW(20, "黄色", "FFA500"),
    /**
     * 红色
     */
    RED(30, "红色", "FF0000"),;

    private Integer code;
    private String desc;
    private String rgb;
    UserQrStatusEnum(Integer code, String desc, String rgb) {
            this.code = code;
            this.desc = desc;
            this.rgb = rgb;
        }
    
    public static UserQrStatusEnum fromCode(Integer code) {
        return Stream.of(UserQrStatusEnum.values()).filter(e -> e.getCode().equals(code)).findFirst().orElse(null);
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
    public String getRgb() {
        return rgb;
    }
}
