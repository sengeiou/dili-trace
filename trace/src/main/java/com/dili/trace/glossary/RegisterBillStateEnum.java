package com.dili.trace.glossary;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * <B>Description</B> 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播 <B>农丰时代科技有限公司</B>
 *
 * @author yuehongbo
 * @createTime 2018/11/8 18:43 1.待审核 2.待采样 3.已采样 4.待检测 5.检测中 6.已检测
 *             7.复检中，8.审核未通过,9.撤销
 */
public enum RegisterBillStateEnum {
	/**
	 * 新建
	 */
	NEW(0, "新建"),
	/**
	 * 待审核
	 */
	WAIT_AUDIT(1, "待审核"), 
	/**
	 * 待采样
	 */
	WAIT_SAMPLE(2, "待采样"), 
	/**
	 * 待检测
	 */
	WAIT_CHECK(4, "待检测"), 
	/**
	 * 检测中
	 */
	CHECKING(5, "检测中"),
	/**
	 * 已检测
	 */
	ALREADY_CHECK(6, "已检测"),
	/**
	 * 已审核
	 */
	ALREADY_AUDIT(7, "已审核"),
	// NO_PASS(8, "审核未通过"),
	// UNDO(9, "撤销"),
	;
	private String name;
	private Integer code;

	RegisterBillStateEnum(Integer code, String name) {
		this.code = code;
		this.name = name;
	}

	public static RegisterBillStateEnum getRegisterBillStateEnum(Integer code) {
		for (RegisterBillStateEnum anEnum : RegisterBillStateEnum.values()) {
			if (anEnum.getCode().equals(code)) {
				return anEnum;
			}
		}
		return null;
	}

	public boolean equalsToCode(Integer code) {
		return this.getCode().equals(code);
	}
	@JsonValue
	public Integer getCode() {
		return code;
	}

	public String getName() {
		return name;
	}
}
