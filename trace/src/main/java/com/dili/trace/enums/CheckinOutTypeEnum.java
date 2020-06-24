package com.dili.trace.enums;

import java.util.stream.Stream;

public enum CheckinOutTypeEnum {
    /**
     * 进门
     */
    IN(10, "进门"),
    /**
     * 出门
     */
    OUT(20, "出门"),;

    private Integer code;
    private String desc;

    CheckinOutTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    public static CheckinOutTypeEnum fromCode(Integer code) {
        return Stream.of(CheckinOutTypeEnum.values()).filter(e -> e.getCode().equals(code)).findFirst().orElse(null);
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
