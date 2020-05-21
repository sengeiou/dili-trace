package com.dili.trace.glossary;

import java.util.stream.Stream;

public enum QrItemTypeEnum {
	/**
	 * 个人信息
	 */
	USER(10, "个人信息", false, ColorEnum.RED),
	/**
	 * 上游信息
	 */
	UPSTREAM(20, "上游信息", true, ColorEnum.RED),
	/**
	 * 登记单信息
	 */
	BILL(30, "登记单信息", false, ColorEnum.YELLOW),
	/**
	 * 其他
	 */
	OTHER(100, "其他", false, ColorEnum.RED),;

	private Integer code;
	private String desc;
	private Boolean addable;
	private ColorEnum defaultColor;

	QrItemTypeEnum(Integer code, String desc, Boolean addable, ColorEnum defaultColor) {
		this.code = code;
		this.desc = desc;
		this.addable = addable;
		this.defaultColor = defaultColor;
	}

	public static QrItemTypeEnum fromCode(Integer code) {
		return Stream.of(QrItemTypeEnum.values()).filter(e -> e.getCode().equals(code)).findFirst().orElse(null);
	}

	public boolean equalsCode(Integer code) {
		return this.getCode().equals(code);

	}

	public ColorEnum getDefaultColor() {
		return defaultColor;
	}

	public Boolean getAddable() {
		return addable;
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
