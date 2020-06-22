package com.dili.trace.enums;

import java.util.Optional;

import one.util.streamex.StreamEx;

/**
 * <B>Description</B> 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播 <B>农丰时代科技有限公司</B>
 *
 * @author wangguofeng
 */
public enum WeightUnitEnum {

	/**
	 * 斤
	 */
	JIN(1, "斤"),
	/**
	 * 公斤
	 */
	KILO(2, "公斤"),
	;

	private String name;
	private Integer code;

	WeightUnitEnum(Integer code, String name) {
		this.code = code;
		this.name = name;
	}

	public static Optional<WeightUnitEnum> fromCode(Integer code) {
		return StreamEx.of(WeightUnitEnum.values()).filterBy(WeightUnitEnum::getCode, code).findFirst();
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
