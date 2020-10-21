package com.dili.trace.enums;

/**
 * @author Guzman
 * @version 1.0
 * @ClassName MeasureTypeEnum
 * 计量类型。10-计件 20-计重。默认计件。
 * @createTime 2020年10月21日 10:14:00
 */
public enum  MeasureTypeEnum {
    COUNT_UNIT(10,"计件"),
    COUNT_WEIGHT(20,"计重"),
    ;


    private Integer code;
    private String name;

    MeasureTypeEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
