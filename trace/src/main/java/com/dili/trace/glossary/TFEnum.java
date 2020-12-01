package com.dili.trace.glossary;

import java.util.Optional;
import java.util.stream.Stream;

public enum TFEnum {
	/**
	 * 是
	 */
	TRUE(1, "是"),
	/**
	 * 否
	 */
	FALSE(0, "否"),
	;

	private Integer code;
	private String desc;

	TFEnum(Integer code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public static Optional<TFEnum> fromCode(Integer code) {
		return Stream.of(TFEnum.values()).filter(e -> e.getCode().equals(code)).findFirst();
	}

	public boolean equalsCode(Integer code) {
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
