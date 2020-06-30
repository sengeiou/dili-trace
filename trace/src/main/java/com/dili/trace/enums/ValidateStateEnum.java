package com.dili.trace.enums;

/**
 * <B>Description</B>
 * 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播
 * <B>农丰时代科技有限公司</B>
 *
 * @author yuehongbo
 * @createTime 2018/11/8 18:43
 */
public enum ValidateStateEnum {
    /**
     * 未实名
     */
    CERTREQ(10, "未实名"),

    /**
     * 待审核
     */
    UNCERT(20, "待审核"),
    /**
     * 审核未通过
     */
    NOPASS(30, "审核未通过"),
    /**
     * 审核通过
     */
    PASSED(40, "审核通过"),
    ;

    private String name;
    private Integer code ;

    ValidateStateEnum(Integer code, String name){
        this.code = code;
        this.name = name;
    }

    public static ValidateStateEnum getEnabledState(Integer code) {
        for (ValidateStateEnum anEnum : ValidateStateEnum.values()) {
            if (anEnum.getCode().equals(code)) {
                return anEnum;
            }
        }
        return null;
    }

    public boolean equalsToCode(Integer code) {
        return this.getCode().equals(code);
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
