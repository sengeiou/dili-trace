package com.dili.trace.glossary;

import java.util.Optional;

import one.util.streamex.StreamEx;

public enum UserTypeEnum {
	USUAL_USER(10, "园区业户"), COMMISSION_USER(20, "园外业户");

	private Integer code;
	private String desc;

	private UserTypeEnum(Integer code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public static Optional<UserTypeEnum> fromCode(Integer code) {
		return StreamEx.of(UserTypeEnum.values()).filterBy(UserTypeEnum::getCode, code).findFirst();
	}

	public boolean equalsToCode(Integer code) {
		return this.getCode().equals(code);
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
}
