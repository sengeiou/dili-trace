package com.dili.trace.enums;

import one.util.streamex.StreamEx;

import java.util.Optional;

/**
 * <B>Description</B> 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播 <B>农丰时代科技有限公司</B>
 *
 * @author wangguofeng
 */
public enum UserActiveEnum {

	/**
	 * 去活跃
	 */
	UP(-1, "去活跃"),
	/**
	 * 活跃
	 */
	DOWN(1, "活跃"),

	;

	private String name;
	private Integer code;

	UserActiveEnum(Integer code, String name) {
		this.code = code;
		this.name = name;
	}

	public static Optional<UserActiveEnum> fromCode(Integer code) {
		return StreamEx.of(UserActiveEnum.values()).filterBy(UserActiveEnum::getCode, code).findFirst();
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
