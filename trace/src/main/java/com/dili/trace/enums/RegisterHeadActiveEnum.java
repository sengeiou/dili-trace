package com.dili.trace.enums;

import one.util.streamex.StreamEx;

import java.util.Optional;


/**
 * @author wangguofeng
 */

public enum RegisterHeadActiveEnum {

	/**
	 * 启用
	 */
	NONE(0, "启用"),
	/**
	 * 禁用
	 */
	DISABLED(1, "禁用"),
	/**
	 * 作废
	 */
	INVALID(2, "作废"),
	;

	private String name;
	private Integer code;

	RegisterHeadActiveEnum(Integer code, String name) {
		this.code = code;
		this.name = name;
	}

	public static Optional<RegisterHeadActiveEnum> fromCode(Integer code) {
		return StreamEx.of(RegisterHeadActiveEnum.values()).filterBy(RegisterHeadActiveEnum::getCode, code).findFirst();
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
