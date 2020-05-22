package com.dili.trace.glossary;

import java.util.stream.Stream;

public enum QrItemActionEnum {
	/**
	 * 审核
	 */
	APPROVE(10, "审核"),
	/**
	 * 增加
	 */
	CREATE(20, "增加"),
	/**
	 * 正常
	 */
	DONOTHING(30, "正常"),
	/**
	 * 正常
	 */
	VIEW(40, "查看"),
	/**
	 * 其他
	 */
	OTHER(100, "其他"),;

	private Integer code;
	private String desc;

	QrItemActionEnum(Integer code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public static QrItemActionEnum fromCode(Integer code) {
		return Stream.of(QrItemActionEnum.values()).filter(e -> e.getCode().equals(code)).findFirst().orElse(null);
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
