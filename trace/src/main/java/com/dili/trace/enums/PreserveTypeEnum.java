package com.dili.trace.enums;

import java.util.Optional;

import one.util.streamex.StreamEx;

/**
 * <B>Description</B> 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播 <B>农丰时代科技有限公司</B>
 *
 * @author wangguofeng
 */
public enum PreserveTypeEnum {
	/**
	 * 鲜活
	 */
	NONE(0, ""),
	/**
	 * 鲜活
	 */
	FRESH(10, "鲜活"),
	/**
	 * 冰鲜
	 */
	ICED(20, "冰鲜"),
	/**
	 * 冻品
	 */
	FROZEN(30, "冻品"),

	/**
	 * 加工品
	 */
	PROCESSING(40, "加工品"),

	;

	private String name;
	private Integer code;

	PreserveTypeEnum(Integer code, String name) {
		this.code = code;
		this.name = name;
	}

	public static Optional<PreserveTypeEnum> fromCode(Integer code) {
		return StreamEx.of(PreserveTypeEnum.values()).filterBy(PreserveTypeEnum::getCode, code).findFirst();
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
