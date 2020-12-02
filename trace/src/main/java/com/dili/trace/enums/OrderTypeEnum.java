package com.dili.trace.enums;

import one.util.streamex.StreamEx;

import java.util.Optional;

/**
 * 订单类型枚举类
 *
 * @author Lily
 */
public enum OrderTypeEnum {

	/**
	 * 报备单
	 */
	REGISTER_BILL(1, "报备单"),
	/**
	 * 进门登记单
	 */
	REGISTER_FORM_BILL(2, "进门登记单"),

	;

	private String name;
	private Integer code;

	OrderTypeEnum(Integer code, String name) {
		this.code = code;
		this.name = name;
	}

	public static Optional<OrderTypeEnum> fromCode(Integer code) {
		return StreamEx.of(OrderTypeEnum.values()).filterBy(OrderTypeEnum::getCode, code).findFirst();
	}

	public boolean equalsToCode(Integer code) {
		return this.getCode().equals(code);
	}

	public static boolean canDoVerify(Integer code) {
		return BillVerifyStatusEnum.WAIT_AUDIT.equalsToCode(code) || BillVerifyStatusEnum.RETURNED.equalsToCode(code);
	}

	public Integer getCode() {
		return code;
	}

	public String getName() {
		return name;
	}
}
