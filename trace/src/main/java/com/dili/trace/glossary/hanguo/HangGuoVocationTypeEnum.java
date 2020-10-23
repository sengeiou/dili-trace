package com.dili.trace.glossary.hanguo;

/**
 * @author asa.lee
 */

public enum HangGuoVocationTypeEnum {
    INDIVIDUAL_WHOLESALE(1, "个体批发"),
    COMMERCIAL_WHOLESALE(2, "商业批发"),
    GROUP(3, "集团"),
    RETAIL(4, "零售"),
    ;

    private String name;
    private Integer code;

    HangGuoVocationTypeEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public static HangGuoVocationTypeEnum getEnabledState(Integer code) {
        for (HangGuoVocationTypeEnum anEnum : HangGuoVocationTypeEnum.values()) {
            if (anEnum.getCode().equals(code)) {
                return anEnum;
            }
        }
        return null;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
