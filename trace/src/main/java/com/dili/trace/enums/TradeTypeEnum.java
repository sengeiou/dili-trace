package com.dili.trace.enums;

import java.util.Optional;

import one.util.streamex.StreamEx;

/**
 * <B>Description</B> 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播 <B>农丰时代科技有限公司</B>
 *
 * @author yuehongbo
 * @createTime 2018/11/8 18:43
 */
public enum TradeTypeEnum {
    NONE(0, "自主报备"),
    SEPARATE_SALES(10, "交易流转"),

    ;

    private String name;
    private Integer code ;

    TradeTypeEnum(Integer code, String name){
        this.code = code;
        this.name = name;
    }

    public static TradeTypeEnum getSalesTypeEnum(Integer code) {
        for (TradeTypeEnum anEnum : TradeTypeEnum.values()) {
            if (anEnum.getCode().equals(code)) {
                return anEnum;
            }
        }
        return null;
    }
	public static Optional<TradeTypeEnum> fromCode(Integer code) {
		return StreamEx.of(TradeTypeEnum.values()).filterBy(TradeTypeEnum::getCode, code).findFirst();
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
