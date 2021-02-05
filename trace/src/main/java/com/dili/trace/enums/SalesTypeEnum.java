package com.dili.trace.enums;

/**
 * <B>Description</B>
 * 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播
 * <B>农丰时代科技有限公司</B>
 *
 * @author yuehongbo
 * @createTime 2018/11/8 18:43
 */
public enum SalesTypeEnum {

    /**
     * 分销
     */
    SEPARATE_SALES(1, "分销"),
    /**
     * 一次全销
     */
    ONE_SALES(2, "一次全销"),
    ;

    private String name;
    private Integer code ;

    SalesTypeEnum(Integer code, String name){
        this.code = code;
        this.name = name;
    }

    public static SalesTypeEnum getSalesTypeEnum(Integer code) {
        for (SalesTypeEnum anEnum : SalesTypeEnum.values()) {
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
