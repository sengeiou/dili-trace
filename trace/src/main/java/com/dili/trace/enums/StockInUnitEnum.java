package com.dili.trace.enums;

import one.util.streamex.StreamEx;

import java.util.Optional;

/**
 * <B>Description</B> 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播 <B>农丰时代科技有限公司</B>
 *
 * @author wangguofeng
 */
public enum StockInUnitEnum {

	/**
	 * 斤
	 */
	JIN(1, "斤"),
	/**
	 * 公斤
	 */
	KILO(2, "公斤"),
	/**
	 * 件
	 */
	PIECE(3, "件"),
	;

	private String name;
	private Integer code;

	StockInUnitEnum(Integer code, String name) {
		this.code = code;
		this.name = name;
	}

	public static Optional<StockInUnitEnum> fromCode(Integer code) {
		return StreamEx.of(StockInUnitEnum.values()).filterBy(StockInUnitEnum::getCode, code).findFirst();
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
