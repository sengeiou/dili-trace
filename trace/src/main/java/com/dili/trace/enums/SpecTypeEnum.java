package com.dili.trace.enums;

import java.util.Optional;

import one.util.streamex.StreamEx;

/**
 * <B>Description</B> 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播 <B>农丰时代科技有限公司</B>
 *
 * @author wangguofeng
 */
public enum SpecTypeEnum {

	/**
	 * 筐
	 */
	BASKET(1, "筐"),
	/**
	 * 箱
	 */
	CARTON(2, "箱"),
	/**
	 * 件
	 */
	PIECE(3, "件"),
	/**
	 * 袋
	 */
	BAG(4, "袋"),

	;

	private String name;
	private Integer code;

	SpecTypeEnum(Integer code, String name) {
		this.code = code;
		this.name = name;
	}

	public static Optional<SpecTypeEnum> fromCode(Integer code) {
		return StreamEx.of(SpecTypeEnum.values()).filterBy(SpecTypeEnum::getCode, code).findFirst();
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
