package com.dili.trace.enums;

import one.util.streamex.StreamEx;

import java.util.Optional;


/**
 * @author asa.lee
 */

public enum BillDeleteStatusEnum {

	/**
	 * 正常
	 */
	NORMAL(0, "正常"),
	/**
	 * 已作废
	 */
	DELETED(1, "已作废"),
	;

	private String name;
	private Integer code;

	BillDeleteStatusEnum(Integer code, String name) {
		this.code = code;
		this.name = name;
	}

	public static Optional<BillDeleteStatusEnum> fromCode(Integer code) {
		return StreamEx.of(BillDeleteStatusEnum.values()).filterBy(BillDeleteStatusEnum::getCode, code).findFirst();
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
