package com.dili.trace.enums;

import java.util.stream.Stream;

public enum CheckinStatusEnum {
    /**
     * 无
     */
    NONE(0, "无"),
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

    CheckinStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    public static CheckinStatusEnum fromCode(Integer code) {
        return Stream.of(CheckinStatusEnum.values()).filter(e -> e.getCode().equals(code)).findFirst().orElse(null);
    }
    public static String getNamefromCode(Integer code) {
        return Stream.ofNullable(CheckinStatusEnum.fromCode(code)).map(CheckinStatusEnum::getDesc).findFirst().orElse("");
    }
    public boolean equalsToCode(Integer code) {
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
