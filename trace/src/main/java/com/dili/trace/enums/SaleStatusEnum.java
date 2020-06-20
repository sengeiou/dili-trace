package com.dili.trace.enums;

import java.util.Optional;

import one.util.streamex.StreamEx;

/**
 * <B>Description</B> 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播 <B>农丰时代科技有限公司</B>
 *
 * @author wangguofeng
 */
public enum SaleStatusEnum {

	/**
	 * 无
	 */
	NONE(0, "无"),
	/**
	 * 可销售
	 */
	FOR_SALE(10, "可销售"),
	/**
	 * 不可销售
	 */
	NOT_FOR_SALE(20, "不可销售"),

	;

	private String name;
	private Integer code;

	SaleStatusEnum(Integer code, String name) {
		this.code = code;
		this.name = name;
	}

	public static Optional<SaleStatusEnum> fromCode(Integer code) {
		return StreamEx.of(SaleStatusEnum.values()).filterBy(SaleStatusEnum::getCode, code).findFirst();
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
