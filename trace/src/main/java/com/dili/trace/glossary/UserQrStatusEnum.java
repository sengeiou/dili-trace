package com.dili.trace.glossary;

import java.util.stream.Stream;

public enum UserQrStatusEnum {
    /**
     * 黑色
     */
    BLACK(0, "黑色", 0xFF000000),
    /**
     * 绿色
     */
    GREEN(10, "绿色", 0xFF17a365),
    /**
     * 黄色
     */
    YELLOW(20, "黄色", 0xFFFFA500),
    /**
     * 红色
     */
    RED(30, "红色", 0xFFFF0000),;

    private Integer code;
    private String desc;
    private Integer argb;
    UserQrStatusEnum(Integer code, String desc, Integer argb) {
            this.code = code;
            this.desc = desc;
            this.argb = argb;
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
    public Integer getARgb() {
        return this.argb;
    }
}
