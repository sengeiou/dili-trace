package com.dili.trace.enums;

/**
 * <B>Description</B>
 * 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播
 * <B>农丰时代科技有限公司</B>
 *
 * @author yuehongbo
 * @createTime 2018/11/8 18:43
 */
public enum VocationTypeEnum {
    WHOLESALE(10, "批发"),
    AGRICULTURETRADE(20, "农贸"),
    GROUP(30, "团体"),
    PERSONAL(40, "个人"),
    CATERER(50, "餐饮"),
    DELIVERIOR(60, "配送商"),
    ;

    private String name;
    private Integer code ;

    VocationTypeEnum(Integer code, String name){
        this.code = code;
        this.name = name;
    }

    public static VocationTypeEnum getEnabledState(Integer code) {
        for (VocationTypeEnum anEnum : VocationTypeEnum.values()) {
            if (anEnum.getCode().equals(code)) {
                return anEnum;
            }
        }
        return null;
    }
    public static String getNameFromCode(Integer code) {
        for (VocationTypeEnum anEnum : VocationTypeEnum.values()) {
            if (anEnum.getCode().equals(code)) {
                return anEnum.getName();
            }
        }
        return "";
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
