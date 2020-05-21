package com.dili.trace.glossary;

import java.util.stream.Stream;

public enum TFEnum {
	/**
	 * true
	 */
	TRUE(1, "true"),
	/**
	 * false
	 */
	FALSE(0, "false"),
	;

	private Integer code;
	private String desc;

	TFEnum(Integer code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public static TFEnum fromCode(Integer code) {
		return Stream.of(TFEnum.values()).filter(e -> e.getCode().equals(code)).findFirst().orElse(null);
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
